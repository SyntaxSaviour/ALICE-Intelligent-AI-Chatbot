package com.farzan;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class that combines AI chat functionality with database logging
 * Integrates with OpenAI API and MySQL database
 */
public class ChatService {
    // Database connection info
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 3306;
    private static final String DB_NAME = "chatbot_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "491811"; // Change this to your actual password
    
    // External API Key - CHANGE THIS TO YOUR ACTUAL API KEY
    private static final String API_KEY = "YOUR_API_KEY_HERE"; // Replace with your actual OpenAI API key
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";
    
    /**
     * Get response from the AI service for a user message
     */
    public String getChatResponse(String userMessage) throws Exception {
        // Create JSON request manually with proper escaping
        String escapedMessage = escapeJson(userMessage);
        String jsonInputString = "{\"model\":\"" + MODEL + "\",\"messages\":[{\"role\":\"user\",\"content\":\"" + escapedMessage + "\"}],\"temperature\":0.7}";
        
        // Create HTTP connection
        URL url = new URL(API_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + API_KEY);
        con.setDoOutput(true);
        
        // Send request
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Check response code
        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            // Read error response
            StringBuilder errorResponse = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
            } catch (Exception e) {
                // Ignore error reading error
            }
            throw new RuntimeException("API request failed with HTTP code: " + responseCode + 
                                     ". Error: " + errorResponse.toString());
        }
        
        // Read response
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        
        // Parse JSON response and extract the reply text
        return parseResponse(response.toString());
    }
    
    /**
     * Parse the JSON response from the AI API and extract the reply text
     */
    private String parseResponse(String jsonResponse) throws Exception {
        // Check for errors first
        if (jsonResponse.contains("\"error\"")) {
            // Simple extraction of error message
            Pattern errorPattern = Pattern.compile("(\"message\":\\s*\"([^\"]*)\")");
            Matcher errorMatcher = errorPattern.matcher(jsonResponse);
            if (errorMatcher.find()) {
                String errorMsg = errorMatcher.group(2);
                throw new RuntimeException("API Error: " + errorMsg);
            } else {
                throw new RuntimeException("Unknown API error");
            }
        }
        
        // Extract the content from the response
        // Looking for pattern: "content":"text here"
        Pattern contentPattern = Pattern.compile("(\"content\":\\s*\"([^\"]*)\")");
        Matcher contentMatcher = contentPattern.matcher(jsonResponse);
        
        if (contentMatcher.find()) {
            String content = contentMatcher.group(2);
            // Unescape common JSON escape sequences
            content = content.replace("\\n", "\n")
                             .replace("\\\"", "\"")
                             .replace("\\\\", "\\");
            return content;
        } else {
            throw new RuntimeException("Could not extract response content from API. Response: " + jsonResponse);
        }
    }
    
    /**
     * Escape special characters in JSON strings
     */
    public String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
    
    /**
     * Establish database connection
     */
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
        String url = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
        return DriverManager.getConnection(url, DB_USER, DB_PASSWORD);
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
     * Retrieve chat history from the database
     */
    public List<ChatMessage> getChatHistory() throws SQLException {
        List<ChatMessage> messages = new ArrayList<>();
        String sql = "SELECT sender, message, ts FROM chat_history ORDER BY ts ASC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String sender = rs.getString("sender");
                String message = rs.getString("message");
                Timestamp timestamp = rs.getTimestamp("ts");
                
                messages.add(new ChatMessage(sender, message, timestamp.toLocalDateTime()));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve chat history from database. " + 
                                 "Database: " + DB_NAME + ", Error: " + e.getMessage(), e);
        }
        
        return messages;
    }
    
    /**
     * Simple class to represent a chat message
     */
    public static class ChatMessage {
        private final String sender;
        private final String message;
        private final LocalDateTime timestamp;
        
        public ChatMessage(String sender, String message, LocalDateTime timestamp) {
            this.sender = sender;
            this.message = message;
            this.timestamp = timestamp;
        }
        
        public String getSender() {
            return sender;
        }
        
        public String getMessage() {
            return message;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
    }
}