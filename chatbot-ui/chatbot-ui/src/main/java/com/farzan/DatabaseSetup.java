package com.farzan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class to set up the database and tables for the chatbot application
 */
public class DatabaseSetup {
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 3306;
    private static final String DB_NAME = "chatbot_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "491811"; // Change this to your actual password
    
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    /**
     * Establish connection without specifying database (for database creation)
     */
    private static Connection getConnectionWithoutDB() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/";
        return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
    }
    
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
     * Create database if it doesn't exist
     */
    public static void createDatabaseIfNotExists() throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
        
        try (Connection conn = getConnectionWithoutDB();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("Database '" + DB_NAME + "' is ready.");
        } catch (SQLException e) {
            throw new SQLException("Failed to create database. " + 
                                 "Error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create the chat_history table if it doesn't exist
     */
    public static void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS chat_history (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "sender VARCHAR(50), " +
                     "message TEXT, " +
                     "ts DATETIME)";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
            System.out.println("Table 'chat_history' is ready.");
        } catch (SQLException e) {
            throw new SQLException("Failed to create table. " + 
                                 "Database: " + DB_NAME + ", Error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Main method to set up the database and table
     */
    public static void main(String[] args) {
        System.out.println("Chatbot Database Setup Utility");
        System.out.println("==============================");
        
        try {
            System.out.println("Setting up database and tables...");
            createDatabaseIfNotExists();
            createTableIfNotExists();
            System.out.println("\nDatabase setup completed successfully!");
            System.out.println("\nYou can now run the chatbot application.");
        } catch (SQLException e) {
            System.err.println("\nDatabase setup failed: " + e.getMessage());
            System.err.println("\nPlease check:");
            System.err.println("1. MySQL is running on your system");
            System.err.println("2. Database credentials are correct");
            System.err.println("3. MySQL JDBC driver is available");
            System.err.println("4. Network connectivity to database server");
            e.printStackTrace();
        }
    }
}