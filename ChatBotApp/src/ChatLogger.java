import java.sql.*;
import java.time.LocalDateTime;

/**
 * Handles logging chat messages to a MySQL database.
 */
public class ChatLogger {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private final String DB_HOST;
    private final int DB_PORT;
    private final String DB_NAME;
    private final String USER;
    private final String PASSWORD;
    
    /**
     * Constructor to initialize database connection parameters
     */
    public ChatLogger(String host, int port, String dbName, String user, String password) {
        this.DB_HOST = host;
        this.DB_PORT = port;
        this.DB_NAME = dbName;
        this.USER = user;
        this.PASSWORD = password;
    }
    
    /**
     * Establish database connection
     */
    private Connection getConnection() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
        return DriverManager.getConnection(url, USER, PASSWORD);
    }
    
    /**
     * Establish connection without specifying database (for database creation)
     */
    private Connection getConnectionWithoutDB() throws SQLException {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/";
        return DriverManager.getConnection(url, USER, PASSWORD);
    }
    
    /**
     * Create database if it doesn't exist
     */
    public void createDatabaseIfNotExists() throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
        
        try (Connection conn = getConnectionWithoutDB();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException("Failed to create database. " + 
                                 "Error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Log a message to the database
     */
    public void logMessage(String sender, String message, LocalDateTime timestamp) throws SQLException {
        String sql = "INSERT INTO chat_history (sender, message, ts) VALUES (?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sender);
            stmt.setString(2, message);
            stmt.setTimestamp(3, Timestamp.valueOf(timestamp));
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to log message to database. " + 
                                 "Database: " + DB_NAME + ", Error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Create the chat_history table if it doesn't exist
     */
    public void createTableIfNotExists() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS chat_history (" +
                     "id INT AUTO_INCREMENT PRIMARY KEY, " +
                     "sender VARCHAR(50), " +
                     "message TEXT, " +
                     "ts DATETIME)";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException("Failed to create table. " + 
                                 "Database: " + DB_NAME + ", Error: " + e.getMessage(), e);
        }
    }
}