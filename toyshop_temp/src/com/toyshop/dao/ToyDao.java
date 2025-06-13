package com.toyshop.dao;

import com.toyshop.bean.Toy;
import com.toyshop.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

// 玩具表操作类
public class ToyDao {

    /**
     * 分页查询所有玩具。
     * @param pageNum 页码 (从1开始)
     * @param pageSize 每页数量
     * @return 当前页的玩具列表
     */
    public List<Toy> findAll(int pageNum, int pageSize) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Toy> toys = new ArrayList<>();
        // 注意: 不同数据库的分页语法不同
        // MySQL: LIMIT offset, count
        // SQL Server: OFFSET offset ROWS FETCH NEXT count ROWS ONLY (SQL Server 2012+)
        // Oracle: 使用子查询和 ROWNUM
        // 这里我们先用 MySQL 的语法作为示例，如果需要SQL Server，需要调整
        String sql = "SELECT toy_id, toy_name, brand, category, price, image_url, description " +
                     "FROM toys ORDER BY toy_id LIMIT ?, ?";
        int offset = (pageNum - 1) * pageSize;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, offset);
            pstmt.setInt(2, pageSize);
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
            System.err.println("分页查询玩具失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return toys;
    }

    /**
     * 返回玩具总数。
     * @return 玩具总记录数
     */
    public int getTotalToyCount() {
        Connection conn = null;
        Statement stmt = null; // 对于简单的无参数查询，Statement也可以
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
            System.err.println("获取玩具总数失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return count;
    }

    /**
     * 根据 ID 查询单个玩具。
     * @param toyId 玩具ID
     * @return Toy 对象或 null
     */
    public Toy findById(int toyId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Toy toy = null;
        String sql = "SELECT toy_id, toy_name, brand, category, price, image_url, description " +
                     "FROM toys WHERE toy_id = ?";

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
            System.err.println("根据ID查询玩具失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return toy;
    }

    /**
     * 根据关键字（玩具名或品牌）和价格范围搜索玩具。
     * @param keyword 搜索关键字 (玩具名或品牌)
     * @param minPrice 最低价格 (如果为0或负数则忽略)
     * @param maxPrice 最高价格 (如果为0或负数则忽略)
     * @return符合条件的玩具列表
     */
    public List<Toy> search(String keyword, double minPrice, double maxPrice) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Toy> toys = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder("SELECT toy_id, toy_name, brand, category, price, image_url, description FROM toys WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append(" AND (toy_name LIKE ? OR brand LIKE ?)");
            params.add("%" + keyword + "%");
            params.add("%" + keyword + "%");
        }
        if (minPrice > 0) {
            sqlBuilder.append(" AND price >= ?");
            params.add(minPrice);
        }
        if (maxPrice > 0) {
            sqlBuilder.append(" AND price <= ?");
            params.add(maxPrice);
        }
        // 可以添加分页逻辑，但题目未明确要求，此处不加

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sqlBuilder.toString());

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
            System.err.println("搜索玩具失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return toys;
    }

    /**
     * 添加新玩具。
     * @param toy 要添加的 Toy 对象
     * @return 添加成功返回 true，否则 false
     */
    public boolean addToy(Toy toy) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO toys (toy_name, brand, category, price, image_url, description) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
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
            System.err.println("添加新玩具失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return success;
    }

    /**
     * 根据 toyId 更新玩具信息。
     * @param toy 包含更新信息的 Toy 对象 (toyId 必须有效)
     * @return 更新成功返回 true，否则 false
     */
    public boolean updateToy(Toy toy) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE toys SET toy_name = ?, brand = ?, category = ?, price = ?, " +
                     "image_url = ?, description = ? WHERE toy_id = ?";
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
            System.err.println("更新玩具信息失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return success;
    }

    /**
     * 根据 toyId 删除玩具。
     * @param toyId 要删除的玩具ID
     * @return 删除成功返回 true，否则 false
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
            System.err.println("删除玩具失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return success;
    }
}
