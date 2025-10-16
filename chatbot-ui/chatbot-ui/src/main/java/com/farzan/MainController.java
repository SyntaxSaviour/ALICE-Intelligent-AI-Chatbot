package com.farzan;
import javafx.scene.Node;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class MainController {

    @FXML private VBox contentVBox;
    @FXML private ScrollPane scrollPane;
    @FXML private TextField inputField;
    @FXML private Button sendButton;

    @FXML private HBox themeBubbleParent; // the HBox that contains the theme bubble (optional to style whole bubble)
    @FXML private StackPane themeBubble;  // the small right-hand indicator StackPane
    @FXML private BorderPane mainContainer; // ensure you already have this


    @FXML private VBox historyList;
    @FXML private VBox chatPane;
    @FXML private VBox historyPane;
    @FXML private ToggleButton themeToggle;
    @FXML private StackPane centerStack;

    // Chat service for AI and database integration
    private ChatService chatService;
    private ExecutorService executorService;

    @FXML
    public void initialize() {
        // Initialize the chat service and executor
        chatService = new ChatService();
        executorService = Executors.newFixedThreadPool(2);
        
        javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.millis(800));
        delay.setOnFinished(event -> showWelcomeMessage());
        delay.play();
        contentVBox.setFillWidth(true);

        // default: chat visible
        showChat();

        // Wire Enter key
        inputField.setOnAction(e -> onSend());

        // Listen for changes in the content VBox height and scroll to bottom
        contentVBox.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setVvalue(1.0);
        });
        
        // Add listener for scroll pane width to update content width
        scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Force the content to resize when the scroll pane resizes
            contentVBox.setMinWidth(newVal.doubleValue() - 20); // Account for padding
        });
    }

    // ---------- Sidebar actions ----------
    @FXML
    private void onChatClicked() {
        showChat();
    }

    @FXML
    private void onHistoryClicked() {
        showHistory();
        // Load actual history from database
        loadHistoryFromDatabase();
    }

    @FXML
    private void onThemeBubbleClicked() {
        boolean nowDark;
        if (mainContainer.getStyleClass().contains("dark-mode")) {
            mainContainer.getStyleClass().remove("dark-mode");
            nowDark = false;
        } else {
            mainContainer.getStyleClass().add("dark-mode");
            nowDark = true;
        }

        // toggle the visual selection class for the theme HBox
        if (nowDark) {
            if (!themeBubbleParent.getStyleClass().contains("theme-active")) {
                themeBubbleParent.getStyleClass().add("theme-active");
            }
        } else {
            themeBubbleParent.getStyleClass().remove("theme-active");
        }
    }

    private void showWelcomeMessage() {
        String welcomeText = """
            👋 Hello there!
            I’m A.L.I.C.E — Artificial Labile Intelligence Cybernetic Existence.
            Your friendly, adaptive chat companion — ready to help you with anything.
            """;

        Label label = new Label(welcomeText);
        label.getStyleClass().add("bubble-bot");
        VBox.setMargin(label, new Insets(4, 0, 4, 0));
        contentVBox.getChildren().add(label);

        // --- ✨ Fade-in animation ---
        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(1200), label);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.setCycleCount(1);
        fade.play();
    }


    private void addBotMessage(String message) {
        Label label = new Label(message);
        label.getStyleClass().add("bubble-bot");
        VBox.setMargin(label, new Insets(6, 0, 6, 0));
        contentVBox.getChildren().add(label);

        animateBubble(label, false); // slide up from left
    }


    // ---------- Navigation helpers ----------
    private void showChat() {
        chatPane.setVisible(true);
        chatPane.setManaged(true);
        historyPane.setVisible(false);
        historyPane.setManaged(false);
    }

    private void showHistory() {
        chatPane.setVisible(false);
        chatPane.setManaged(false);
        historyPane.setVisible(true);
        historyPane.setManaged(true);
    }

    // ---------- Sending & message rendering ----------
    @FXML
    private void onSend() {
        String input = inputField.getText();
        if (input == null) return;
        input = input.trim();
        if (input.isEmpty()) return;

        final String message = input;   // final for lambda use
        addMessage(message, true);
        inputField.clear();

        // Process message with AI service
        processUserMessage(message);
    }

    private void processUserMessage(String message) {
        // Log user message to database
        executorService.submit(() -> {
            try {
                chatService.logMessage("user", message, java.time.LocalDateTime.now());
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Database error when saving user message: " + e.getMessage());
                });
            }
        });

        // Disable input while processing
        Platform.runLater(() -> {
            sendButton.setDisable(true);
            inputField.setDisable(true);
        });

        // Get bot response asynchronously
        executorService.submit(() -> {
            try {
                String reply = chatService.getChatResponse(message);
                Platform.runLater(() -> {
                    addMessage(reply, false);
                    // Re-enable input
                    sendButton.setDisable(false);
                    inputField.setDisable(false);
                });

                // Log bot response to database
                try {
                    chatService.logMessage("assistant", reply, java.time.LocalDateTime.now());
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        showError("Database error when saving bot response: " + e.getMessage());
                    });
                }
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Failed to get response: " + e.getMessage());
                    addMessage("Sorry, I'm having trouble responding right now.", false);
                    // Re-enable input
                    sendButton.setDisable(false);
                    inputField.setDisable(false);
                });
            }
        });
    }

    private void addMessage(String text, boolean isUser) {
        Platform.runLater(() -> {
            Label bubble = new Label(text);
            bubble.setWrapText(true);
            // Update max width based on scroll pane width
            bubble.setMaxWidth(scrollPane.getWidth() * 0.7); // 70% of scroll pane width
            bubble.getStyleClass().add(isUser ? "bubble-user" : "bubble-bot");

            Label ts = new Label(formatTimestamp());
            ts.getStyleClass().add("timestamp");

            VBox bubbleBox = new VBox(bubble, ts);
            bubbleBox.setSpacing(6);
            bubbleBox.setAlignment(Pos.CENTER_LEFT);
            bubbleBox.setMaxWidth(scrollPane.getWidth() * 0.7); // 70% of scroll pane width
            HBox.setHgrow(bubbleBox, Priority.NEVER);

            HBox wrapper = new HBox();
            wrapper.setPadding(new Insets(6, 0, 6, 0));
            wrapper.prefWidthProperty().bind(contentVBox.widthProperty());
            wrapper.setAlignment(Pos.CENTER);

            if (isUser) {
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                HBox.setMargin(bubbleBox, new Insets(0, 12, 0, 0));
                wrapper.getChildren().addAll(spacer, bubbleBox);
                wrapper.setAlignment(Pos.CENTER_RIGHT);
            } else {
                wrapper.getChildren().addAll(bubbleBox);
                wrapper.setAlignment(Pos.CENTER_LEFT);
            }

            // initially invisible / offset for animation
            wrapper.setOpacity(0);
            wrapper.setTranslateY(12);
            wrapper.setTranslateX(isUser ? 30 : -30);

            contentVBox.getChildren().add(wrapper);

            // animate wrapper (so whole bubble+timestamp moves together)
            animateBubble(wrapper, isUser);
        });
    }

    // fade + slide animation for a message node; fromRight = true for user messages
    private void animateBubble(Node node, boolean fromRight) {
        // Fade transition
        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(420), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        // Slide (translate) transition
        javafx.animation.TranslateTransition slide = new javafx.animation.TranslateTransition(javafx.util.Duration.millis(420), node);
        slide.setFromY(12);
        slide.setToY(0);
        slide.setFromX(fromRight ? 30 : -30);
        slide.setToX(0);

        // Optional slight pop (scale) for a touch of polish
        javafx.animation.ScaleTransition pop = new javafx.animation.ScaleTransition(javafx.util.Duration.millis(300), node);
        pop.setFromX(0.985);
        pop.setFromY(0.985);
        pop.setToX(1.0);
        pop.setToY(1.0);

        javafx.animation.ParallelTransition t = new javafx.animation.ParallelTransition(fade, slide, pop);
        t.play();
    }


    private String formatTimestamp() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm a");
        return now.format(fmt);
    }

    // ---------- History UI with database integration ----------
    private void loadHistoryFromDatabase() {
        historyList.getChildren().clear();
        
        // Load history from database asynchronously
        executorService.submit(() -> {
            try {
                List<ChatService.ChatMessage> messages = chatService.getChatHistory();
                Platform.runLater(() -> {
                    for (ChatService.ChatMessage msg : messages) {
                        String senderDisplay = msg.getSender().equals("user") ? "You" : "AI";
                        addToHistoryItem(msg.getTimestamp().toString().substring(0, 16) + " — " + 
                                       senderDisplay + ": " + msg.getMessage());
                    }
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    showError("Failed to load chat history: " + e.getMessage());
                    // Show placeholder if database fails
                    loadHistoryPlaceholder();
                });
            }
        });
    }

    private void loadHistoryPlaceholder() {
        historyList.getChildren().clear();
        // sample / placeholder items
        addToHistoryItem("2025-10-08 06:30 — You: Hello");
        addToHistoryItem("2025-10-08 06:32 — AI: Hello! I am Alice.");
        addToHistoryItem("2025-10-08 06:33 — You: Summarize my notes");
    }

    private void addToHistoryItem(String text) {
        Label lbl = new Label(text);
        lbl.setWrapText(true);
        lbl.getStyleClass().add("history-item");
        lbl.setPadding(new Insets(8));
        lbl.setMaxWidth(760);
        historyList.getChildren().add(lbl);
    }

    private String currentTimestamp() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.format.DateTimeFormatter f = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return now.format(f);
    }
    
    /**
     * Show error message dialog
     */
    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}
