package com.farzan;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Test class for ChatService functionality
 */
public class TestChatService {
    
    public static void main(String[] args) {
        System.out.println("Testing ChatService...");
        
        try {
            ChatService chatService = new ChatService();
            
            // Test database connection by logging a test message
            System.out.println("Testing database connection...");
            chatService.logMessage("test", "This is a test message", LocalDateTime.now());
            System.out.println("Database connection successful!");
            
            // Test retrieving chat history
            System.out.println("Testing chat history retrieval...");
            java.util.List<ChatService.ChatMessage> messages = chatService.getChatHistory();
            System.out.println("Retrieved " + messages.size() + " messages from database");
            
            // Show first few messages
            int count = 0;
            for (ChatService.ChatMessage msg : messages) {
                if (count++ < 3) {
                    System.out.println("  [" + msg.getTimestamp() + "] " + msg.getSender() + ": " + msg.getMessage());
                }
            }
            
            System.out.println("\nAll tests passed!");
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}