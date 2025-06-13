package com.toyshop.dao;

import com.toyshop.bean.User;
import com.toyshop.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    /**
     * Finds a user by their username.
     * @param username The username to search for.
     * @return User object if found, null otherwise.
     */
    public User findByUsername(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        String sql = "SELECT user_id, username, password, role FROM users WHERE username = ?";

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password")); // Be cautious with password handling
                user.setRole(rs.getInt("role"));
            }
        } catch (SQLException e) {
            System.err.println("Error finding user by username: " + username + " - " + e.getMessage());
            // In a real app, consider logging framework (e.g., SLF4j + Logback)
            // and possibly rethrowing as a custom DAOException
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return user;
    }

    /**
     * Adds a new user to the database.
     * Assumes the password in the User object is already hashed if needed.
     * The role is typically set to a default value (e.g., 1 for normal user) before calling this.
     * @param user The User object containing information for the new user.
     * @return true if the user was added successfully, false otherwise.
     */
    public boolean addUser(User user) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        boolean success = false;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword()); // Store password as is; hashing should be handled by the service/servlet layer
            pstmt.setInt(3, user.getRole());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding user: " + user.getUsername() + " - " + e.getMessage());
            // Check for unique constraint violation (e.g., duplicate username)
            // SQLState for unique constraint violation can vary by DB (e.g., 23000, 23505)
            // if (e.getSQLState().startsWith("23")) { /* handle duplicate username */ }
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return success;
    }

    // Example of checking if a username exists (can be useful for registration)
    public boolean usernameExists(String username) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT 1 FROM users WHERE username = ?";
        boolean exists = false;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                exists = true;
            }
        } catch (SQLException e) {
            System.err.println("Error checking if username exists: " + username + " - " + e.getMessage());
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return exists;
    }
}
