# Chat Window Resizing Fix

This document explains the changes made to fix the issue where the chat area didn't expand when the window was maximized.

## Problem

When the chat window was maximized, the chat area remained small because:
1. Fixed width values were set in the FXML file
2. The CSS had a fixed width for the content column
3. Message bubbles had fixed maximum widths


## Solution

### 1. FXML Changes ([main.fxml](chatbot-ui/chatbot-ui/src/main/resources/ui/main.fxml))

Removed fixed `prefWidth` and `prefHeight` values and added `VBox.vgrow="ALWAYS"` to make elements expand:

```xml
<!-- Before -->
<VBox fx:id="chatPane" spacing="12" alignment="TOP_CENTER" styleClass="chat-panel" prefWidth="820" prefHeight="560">

<!-- After -->
<VBox fx:id="chatPane" spacing="12" alignment="TOP_CENTER" styleClass="chat-panel" VBox.vgrow="ALWAYS">
```

### 2. CSS Changes ([theme.css](chatbot-ui/chatbot-ui/src/main/resources/css/theme.css))

Removed the fixed width from the content column:

```css
/* Before */
.content-column {
    -fx-padding: 18;
    -fx-alignment: TOP_CENTER;
    -fx-pref-width: 780; /* This line was removed */
}

/* After */
.content-column {
    -fx-padding: 18;
    -fx-alignment: TOP_CENTER;
    /* No fixed width - allows resizing */
}
```

### 3. Java Code Changes ([MainController.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/MainController.java))

Added dynamic width calculation for message bubbles and a width listener:

1. Added width listener to update content when the scroll pane is resized:
```java
// Add listener for scroll pane width to update content width
scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
    // Force the content to resize when the scroll pane resizes
    contentVBox.setMinWidth(newVal.doubleValue() - 20); // Account for padding
});
```

2. Updated the addMessage method to use dynamic widths:
```java
// Update max width based on scroll pane width
bubble.setMaxWidth(scrollPane.getWidth() * 0.7); // 70% of scroll pane width
bubbleBox.setMaxWidth(scrollPane.getWidth() * 0.7); // 70% of scroll pane width
```

## Testing the Fix

To test the fix:

1. Compile the application:
   ```
   cd chatbot-ui\chatbot-ui
   .\mvn.bat clean compile
   ```

2. Run the application:
   ```
   .\mvn.bat javafx:run
   ```

3. Maximize the window and verify that:
   - The chat area expands to fill the available space
   - Message bubbles adjust their width appropriately
   - The input area expands to fill the width

## Expected Behavior

After applying these changes:
- When the window is maximized, the chat area will expand to fill the available space
- Message bubbles will maintain a reasonable width (70% of the scroll pane width)
- The layout will be responsive to window resizing
- Both the light and dark themes will work correctly with the new layout

## Additional Notes

The changes maintain the original visual design while making the layout responsive. The 70% width for message bubbles ensures they don't become too wide on large screens while still making good use of available space.