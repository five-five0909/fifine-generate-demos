package com.toyshop.dao;

import com.toyshop.bean.User;
import com.toyshop.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// 用户表操作类
public class UserDao {

    /**
     * 根据用户名查询用户。
     * @param username 用户名
     * @return 如果找到用户，则返回 User 对象；否则返回 null。
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
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getInt("role"));
            }
        } catch (SQLException e) {
            System.err.println("根据用户名查询用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return user;
    }

    /**
     * 向数据库插入一个新用户。
     * @param user 要添加的 User 对象 (其中 userId 通常是自增的，不需要手动设置)
     * @return 如果用户添加成功，则返回 true；否则返回 false。
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
            pstmt.setString(2, user.getPassword());
            pstmt.setInt(3, user.getRole());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                success = true;
            }
        } catch (SQLException e) {
            // 需要处理用户名唯一约束可能导致的异常 (SQLIntegrityConstraintViolationException)
            System.err.println("添加新用户失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
        return success;
    }
}
