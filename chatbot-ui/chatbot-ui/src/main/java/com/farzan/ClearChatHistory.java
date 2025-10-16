package com.farzan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to clear chat history from the database
 */
public class ClearChatHistory {
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 3306;
    private static final String DB_NAME = "chatbot_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "491811"; // Change this to your actual password
    
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Establish database connection
     */
    private static Connection getConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
        return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
    }

    /**
     * Clear all chat history from the database
     */
    public static void clearChatHistory() throws SQLException {
        String sql = "DELETE FROM chat_history";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            int rowsDeleted = stmt.executeUpdate(sql);
            System.out.println("Cleared " + rowsDeleted + " messages from chat history.");
        } catch (SQLException e) {
            throw new SQLException("Failed to clear chat history from database. " + 
                                 "Database: " + DB_NAME + ", Error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Main method to clear chat history
     */
    public static void main(String[] args) {
        System.out.println("Clearing Chat History Utility");
        System.out.println("============================");
        
        try {
            clearChatHistory();
            System.out.println("\nChat history cleared successfully!");
        } catch (SQLException e) {
            System.err.println("\nFailed to clear chat history: " + e.getMessage());
            System.err.println("\nPlease check:");
            System.err.println("1. MySQL is running on your system");
            System.err.println("2. Database credentials are correct");
            System.err.println("3. MySQL JDBC driver is available");
            System.err.println("4. Network connectivity to database server");
            e.printStackTrace();
        }
    }
}