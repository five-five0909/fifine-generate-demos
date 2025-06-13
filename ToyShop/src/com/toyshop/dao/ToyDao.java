package com.toyshop.dao;

import com.toyshop.bean.Toy;
import com.toyshop.util.DBUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ToyDao {

    /**
     * Finds all toys with pagination.
     * @param pageNum The current page number (1-based).
     * @param pageSize The number of toys per page.
     * @return A list of Toy objects for the current page.
     */
    public List<Toy> findAll(int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Toy> toys = new ArrayList<>();
        // Assuming MySQL syntax for LIMIT and OFFSET.
        // For SQL Server: OFFSET (pageNum - 1) * pageSize ROWS FETCH NEXT pageSize ROWS ONLY
        // For Oracle: Use a subquery with ROWNUM
        String sql = "SELECT toy_id, toy_name, brand, category, price, image_url, description FROM toys ORDER BY toy_id LIMIT ? OFFSET ?";
        int offset = (pageNum - 1) * pageSize;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, offset);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Toy toy = new Toy();
                toy.setToyId(rs.getInt("toy_id"));
                toy.setToyName(rs.getString("toy_name"));
                toy.setBrand(rs.getString("brand"));
                toy.setCategory(rs.getString("category"));
                toy.setPrice(rs.getBigDecimal("price"));
                toy.setImageUrl(rs.getString("image_url"));
                toy.setDescription(rs.getString("description"));
                toys.add(toy);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all toys (paginated): " + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return toys;
    }

    /**
     * Gets the total count of toys in the database.
     * @return The total number of toys.
     */
    public int getTotalToyCount() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT COUNT(*) FROM toys";
        int count = 0;

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error getting total toy count: " + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return count;
    }

    /**
     * Finds a single toy by its ID.
     * @param toyId The ID of the toy to find.
     * @return Toy object if found, null otherwise.
     */
    public Toy findById(int toyId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Toy toy = null;
        String sql = "SELECT toy_id, toy_name, brand, category, price, image_url, description FROM toys WHERE toy_id = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, toyId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                toy = new Toy();
                toy.setToyId(rs.getInt("toy_id"));
                toy.setToyName(rs.getString("toy_name"));
                toy.setBrand(rs.getString("brand"));
                toy.setCategory(rs.getString("category"));
                toy.setPrice(rs.getBigDecimal("price"));
                toy.setImageUrl(rs.getString("image_url"));
                toy.setDescription(rs.getString("description"));
            }
        } catch (SQLException e) {
            System.err.println("Error finding toy by ID: " + toyId + " - " + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return toy;
    }

    /**
     * Searches for toys based on a keyword (name or brand) and price range.
     * @param keyword The keyword to search in toy name or brand.
     * @param minPrice The minimum price.
     * @param maxPrice The maximum price.
     * @return A list of matching Toy objects.
     */
    public List<Toy> search(String keyword, Double minPrice, Double maxPrice) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Toy> toys = new ArrayList<>();

        // Build SQL query dynamically based on provided parameters
        StringBuilder sqlBuilder = new StringBuilder("SELECT toy_id, toy_name, brand, category, price, image_url, description FROM toys WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append(" AND (LOWER(toy_name) LIKE LOWER(?) OR LOWER(brand) LIKE LOWER(?))");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (minPrice != null) {
            sqlBuilder.append(" AND price >= ?");
            params.add(minPrice);
        }
        if (maxPrice != null && maxPrice > 0) { // maxPrice could be 0 if not set, or a very large number
            sqlBuilder.append(" AND price <= ?");
            params.add(maxPrice);
        }
        sqlBuilder.append(" ORDER BY toy_name"); // Or any other preferred order

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sqlBuilder.toString());

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            rs = pstmt.executeQuery();
            while (rs.next()) {
                Toy toy = new Toy();
                toy.setToyId(rs.getInt("toy_id"));
                toy.setToyName(rs.getString("toy_name"));
                toy.setBrand(rs.getString("brand"));
                toy.setCategory(rs.getString("category"));
                toy.setPrice(rs.getBigDecimal("price"));
                toy.setImageUrl(rs.getString("image_url"));
                toy.setDescription(rs.getString("description"));
                toys.add(toy);
            }
        } catch (SQLException e) {
            System.err.println("Error searching toys: " + e.getMessage());
            e.printStackTrace(); // More detailed log for debugging search issues
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return toys;
    }

    /**
     * Adds a new toy to the database.
     * @param toy The Toy object containing information for the new toy.
     * @return true if the toy was added successfully, false otherwise.
     */
    public boolean addToy(Toy toy) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO toys (toy_name, brand, category, price, image_url, description) VALUES (?, ?, ?, ?, ?, ?)";
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, toy.getToyName());
            pstmt.setString(2, toy.getBrand());
            pstmt.setString(3, toy.getCategory());
            pstmt.setBigDecimal(4, toy.getPrice());
            pstmt.setString(5, toy.getImageUrl());
            pstmt.setString(6, toy.getDescription());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding toy: " + toy.getToyName() + " - " + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return success;
    }

    /**
     * Updates an existing toy's information in the database.
     * @param toy The Toy object containing updated information. Must have toyId set.
     * @return true if the toy was updated successfully, false otherwise.
     */
    public boolean updateToy(Toy toy) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE toys SET toy_name = ?, brand = ?, category = ?, price = ?, image_url = ?, description = ? WHERE toy_id = ?";
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, toy.getToyName());
            pstmt.setString(2, toy.getBrand());
            pstmt.setString(3, toy.getCategory());
            pstmt.setBigDecimal(4, toy.getPrice());
            pstmt.setString(5, toy.getImageUrl());
            pstmt.setString(6, toy.getDescription());
            pstmt.setInt(7, toy.getToyId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            System.err.println("Error updating toy: " + toy.getToyId() + " - " + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return success;
    }

    /**
     * Deletes a toy from the database by its ID.
     * @param toyId The ID of the toy to delete.
     * @return true if the toy was deleted successfully, false otherwise.
     */
    public boolean deleteToy(int toyId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM toys WHERE toy_id = ?";
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, toyId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            System.err.println("Error deleting toy: " + toyId + " - " + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return success;
    }

    /**
     * Fetches all toys, typically for admin purposes where pagination might not be strictly needed or is simpler.
     * @return A list of all Toy objects.
     */
    public List<Toy> findAllForAdmin() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<Toy> toys = new ArrayList<>();
        String sql = "SELECT toy_id, toy_name, brand, category, price, image_url, description FROM toys ORDER BY toy_id";

        try {
            conn = DBUtil.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Toy toy = new Toy();
                toy.setToyId(rs.getInt("toy_id"));
                toy.setToyName(rs.getString("toy_name"));
                toy.setBrand(rs.getString("brand"));
                toy.setCategory(rs.getString("category"));
                toy.setPrice(rs.getBigDecimal("price"));
                toy.setImageUrl(rs.getString("image_url"));
                toy.setDescription(rs.getString("description"));
                toys.add(toy);
            }
        } catch (SQLException e) {
            System.err.println("Error finding all toys for admin: " + e.getMessage());
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return toys;
    }
}
