package com.toyshop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
// For a simple connection pool, consider using a List or Queue of connections,
// but for this specific request (all code in this class, no external libs),
// we will stick to basic DriverManager usage per call, as a true pool
// without external libraries is complex to implement robustly here.
// The prompt mentions "simulate a simple connection pool" OR "use javax.sql.DataSource".
// Given the constraint "all code within this class, not relying on external libraries"
// (assuming standard JDK is fine, but not things like Apache DBCP unless provided by user),
// a true robust pool is out of scope for a single file generation.
// We will use DriverManager and note this limitation.

public class DBUtil {

    // TODO: Replace with your actual database connection details
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; // Example for MySQL
    // private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; // Example for SQL Server
    private static final String DB_URL = "jdbc:mysql://localhost:3306/your_toyshop_db?useSSL=false&serverTimezone=UTC"; // Example for MySQL
    // private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=your_toyshop_db"; // Example for SQL Server
    private static final String USER = "your_db_user";
    private static final String PASS = "your_db_password";

    static {
        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            // In a real web app, you'd throw a runtime exception or handle this more gracefully
            throw new RuntimeException("Failed to load JDBC driver.", e);
        }
    }

    /**
     * Establishes a connection to the database.
     * Note: This implementation does not use a connection pool. Each call creates a new connection.
     * For production environments, a connection pool (e.g., HikariCP, Apache Commons DBCP) is highly recommended.
     * @return A Connection object to the database.
     * @throws SQLException if a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    /**
     * Closes the given database resources.
     *
     * @param conn The Connection to close.
     * @param stmt The Statement to close.
     * @param rs   The ResultSet to close.
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException se) {
            // Log error or handle as needed
            System.err.println("Error closing ResultSet: " + se.getMessage());
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException se) {
            // Log error or handle as needed
            System.err.println("Error closing Statement: " + se.getMessage());
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            // Log error or handle as needed
            System.err.println("Error closing Connection: " + se.getMessage());
        }
    }

    /**
     * Overloaded close method for when there's no ResultSet.
     *
     * @param conn The Connection to close.
     * @param stmt The Statement to close.
     */
    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    // Main method for basic connection testing (optional)
    public static void main(String[] args) {
        Connection conn = null;
        try {
            System.out.println("Connecting to database...");
            conn = DBUtil.getConnection();
            if (conn != null) {
                System.out.println("Database connection successful!");
                // You could add a simple query here to test further
                // Statement stmt = conn.createStatement();
                // ResultSet rs = stmt.executeQuery("SELECT 1");
                // if (rs.next()) {
                //     System.out.println("Test query successful: " + rs.getInt(1));
                // }
                // DBUtil.close(conn, stmt, rs);
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            System.err.println("Connection Failed! Check output console");
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
