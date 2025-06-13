package com.toyshop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList; // 用于模拟连接池

// 数据库连接池工具类
public class DBUtil {

    // 数据库连接参数 - 请根据您的实际配置修改这些占位符
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=your_db_name;encrypt=false"; // encrypt=false 适用于某些SQL Server驱动版本避免SSL问题
    private static final String USER = "your_db_user";
    private static final String PASSWORD = "your_db_password";

    // 模拟的数据库连接池
    private static final LinkedList<Connection> connectionPool = new LinkedList<>();
    private static final int INITIAL_POOL_SIZE = 5; // 初始连接数
    private static final int MAX_POOL_SIZE = 10;    // 最大连接数

    // 静态代码块，用于加载驱动程序和初始化连接池
    static {
        try {
            // 加载JDBC驱动程序 (对于SQL Server, 如果是JDBC 4.0+驱动，此步骤通常是可选的)
            // Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver"); // 取消注释如果需要显式加载
            initializePool();
        } catch (Exception e) {
            System.err.println("数据库驱动加载或连接池初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 初始化连接池
    private static synchronized void initializePool() throws SQLException {
        if (connectionPool.isEmpty()) {
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                connectionPool.add(createConnection());
            }
        }
    }

    // 创建一个新的数据库连接
    private static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 从连接池获取一个数据库连接。
     * @return 数据库连接对象
     * @throws SQLException 如果获取连接失败
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (getTotalConnectionsInSystem() < MAX_POOL_SIZE) { // 这里的getTotalConnectionsInSystem需要更复杂的逻辑来跟踪活跃连接，此处简化
                return createConnection(); // 池空且未达上限，创建新连接（不放入池中，或放入后立即取出）
            } else {
                throw new SQLException("连接池已满，无法获取连接。");
            }
        }
        // 从池中取出一个连接
        Connection conn = connectionPool.poll(); // poll() 获取并移除第一个元素
        // 简单校验连接是否仍然有效 (可选，可能影响性能)
        if (conn == null || conn.isClosed() || !conn.isValid(1)) {
            try { conn.close(); } catch (Exception e) { /* 忽略关闭错误 */ }
            return createConnection(); // 如果连接无效，创建一个新的
        }
        return conn;
    }

    /**
     * 将数据库连接归还到连接池。
     * @param conn 要归还的连接
     */
    public static synchronized void releaseConnection(Connection conn) {
        if (conn != null) {
            try {
                // 检查连接池是否已满
                if (connectionPool.size() < MAX_POOL_SIZE) {
                    if (!conn.isClosed()) { // 确保连接未关闭
                        connectionPool.offer(conn); // offer() 将连接添加到池尾
                    }
                } else {
                    conn.close(); // 池满则关闭此连接
                }
            } catch (SQLException e) {
                System.err.println("归还连接到池中失败: " + e.getMessage());
                try {
                    conn.close(); // 尝试关闭
                } catch (SQLException ex) {
                    // 忽略
                }
            }
        }
    }

    // 辅助方法，模拟获取系统中总连接数（在实际应用中会更复杂）
    private static int getTotalConnectionsInSystem() {
        // 这是一个简化的示例。一个真实的连接池需要更精确地跟踪所有活跃的（池内和池外）连接。
        // 这里我们仅基于池的大小来判断，这并不完全准确。
        return connectionPool.size(); // 简化的示例
    }


    /**
     * 关闭数据库资源。
     * @param conn Connection 对象
     * @param stmt Statement 对象 (或 PreparedStatement)
     * @param rs ResultSet 对象
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            System.err.println("关闭 ResultSet 失败: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                System.err.println("关闭 Statement 失败: " + e.getMessage());
            } finally {
                if (conn != null) {
                    releaseConnection(conn); // 将连接归还到池中，而不是直接关闭
                }
            }
        }
    }

    // 重载 close 方法，用于不需要 ResultSet 的场景
    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    // (可选) 程序关闭时清理连接池
    public static synchronized void shutdownPool() {
        for (Connection conn : connectionPool) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("关闭连接池中的连接失败: " + e.getMessage());
            }
        }
        connectionPool.clear();
        System.out.println("数据库连接池已关闭。");
    }
}
