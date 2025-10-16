import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main chat window for the chatbot application.
 * Provides UI components and handles user interactions.
 */
public class ChatWindow extends JFrame {
    // Database connection info - configurable constants
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 3306;
    private static final String DB_NAME = "chatbot_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "491811"; // Change this to your actual password
    
    // External API Key - CHANGE THIS TO YOUR ACTUAL API KEY
    private static final String API_KEY = "YOUR_API_KEY_HERE"; // Replace with your actual OpenAI API key
    
    // UI Components
    private JTextArea conversationArea;
    private JTextField inputField;
    private JButton sendButton;
    
    // Backend services
    private ChatLogger chatLogger;
    private AiServiceClient aiServiceClient;
    
    // Thread pool for async operations
    private ExecutorService executorService;
    
    public ChatWindow() {
        initializeServices();
        initializeUI();
        displayWelcomeMessage();
    }
    
    /**
     * Initialize backend services
     */
    private void initializeServices() {
        try {
            chatLogger = new ChatLogger(DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD);
            // Create database and table if they don't exist
            chatLogger.createDatabaseIfNotExists();
            chatLogger.createTableIfNotExists();
            aiServiceClient = new AiServiceClient(API_KEY);
            executorService = Executors.newFixedThreadPool(2);
        } catch (Exception e) {
            showError("Failed to initialize services: " + e.getMessage());
            e.printStackTrace(); // For debugging
        }
    }
    
    /**
     * Set up the user interface
     */
    private void initializeUI() {
        setTitle("Chat Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // Center the window
        
        // Create components
        conversationArea = new JTextArea();
        conversationArea.setEditable(false);
        conversationArea.setWrapStyleWord(true);
        conversationArea.setLineWrap(true);
        conversationArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(conversationArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        inputField = new JTextField();
        sendButton = new JButton("Send");
        
        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Event handlers
        sendButton.addActionListener(new SendButtonListener());
        inputField.addActionListener(new SendButtonListener());
    }
    
    /**
     * Display welcome message when app starts
     */
    private void displayWelcomeMessage() {
        appendToConversation("Assistant", "Hello! How can I help you today?");
    }
    
    /**
     * Handle sending messages
     */
    private class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String userMessage = inputField.getText().trim();
            if (!userMessage.isEmpty()) {
                processUserMessage(userMessage);
                inputField.setText("");
            }
        }
    }
    
    /**
     * Process user message: log to DB, get bot response, display both
     */
    private void processUserMessage(String message) {
        // Display user message immediately
        appendToConversation("You", message);
        
        // Log user message to database
        executorService.submit(() -> {
            try {
                chatLogger.logMessage("user", message, LocalDateTime.now());
            } catch (SQLException e) {
                SwingUtilities.invokeLater(() -> 
                    showError("Database error when saving user message: " + e.getMessage()));
            }
        });
        
        // Disable input while processing
        setInputEnabled(false);
        
        // Get bot response asynchronously
        executorService.submit(() -> {
            try {
                String botResponse = aiServiceClient.getChatResponse(message);
                SwingUtilities.invokeLater(() -> {
                    appendToConversation("Assistant", botResponse);
                    setInputEnabled(true);
                });
                
                // Log bot response to database
                try {
                    chatLogger.logMessage("assistant", botResponse, LocalDateTime.now());
                } catch (SQLException e) {
                    SwingUtilities.invokeLater(() -> 
                        showError("Database error when saving bot response: " + e.getMessage()));
                }
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    showError("Failed to get response: " + e.getMessage());
                    appendToConversation("Assistant", "Sorry, I'm having trouble responding right now.");
                    setInputEnabled(true);
                });
            }
        });
    }
    
    /**
     * Append message to conversation area
     */
    private void appendToConversation(String sender, String message) {
        String timestamp = java.time.LocalTime.now().toString().substring(0, 8);
        String formattedMessage = String.format("[%s] %s: %s%n", timestamp, sender, message);
        
        conversationArea.append(formattedMessage);
        conversationArea.setCaretPosition(conversationArea.getDocument().getLength());
    }
    
    /**
     * Enable/disable input components
     */
    private void setInputEnabled(boolean enabled) {
        inputField.setEnabled(enabled);
        sendButton.setEnabled(enabled);
    }
    
    /**
     * Show error message dialog
     */
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Main entry point
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel
            }
            
            new ChatWindow().setVisible(true);
        });
    }
}