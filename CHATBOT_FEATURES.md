# Chatbot Application - New Features Summary

This document summarizes the new features and utilities added to make the chatbot application easier to use.

## 1. Chat History Management

### Clear Chat History
- **New Utility**: [ClearChatHistory.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/ClearChatHistory.java)
- **Batch Script**: [clear-chat.bat](chatbot-ui/chatbot-ui/clear-chat.bat)
- **Maven Command**: `mvn compile exec:java@clear-chat-history`

To clear all chat history from the database:
1. Double-click [clear-chat.bat](chatbot-ui/chatbot-ui/clear-chat.bat)
2. Or run the Maven command from the chatbot-ui directory

## 2. Single-Click Application Launch

### Multiple Launch Options
1. **Standard Launch with Console**: [launch-chatbot.bat](chatbot-ui/chatbot-ui/launch-chatbot.bat)
   - Shows compilation and runtime output
   - Good for troubleshooting

2. **Silent Launch**: [ChatbotLauncher.bat](chatbot-ui/chatbot-ui/ChatbotLauncher.bat)
   - Hides the console window
   - Cleaner user experience

3. **Desktop Shortcut**: 
   - Run [install-shortcut.bat](chatbot-ui/chatbot-ui/install-shortcut.bat) to create
   - Creates a "Chatbot" icon on your desktop
   - Double-click to launch the application

### How It Works
All launch scripts:
1. Automatically set up the Maven environment
2. Compile the application
3. Launch the JavaFX interface
4. Handle errors gracefully

## 3. Desktop Integration

### Desktop Shortcut Creation
- **PowerShell Script**: [create-shortcut.ps1](chatbot-ui/chatbot-ui/create-shortcut.ps1)
- **Batch Wrapper**: [install-shortcut.bat](chatbot-ui/chatbot-ui/install-shortcut.bat)

To create a desktop shortcut:
1. Double-click [install-shortcut.bat](chatbot-ui/chatbot-ui/install-shortcut.bat)
2. A "Chatbot" icon will appear on your desktop
3. Double-click the icon to launch the application

## 4. Updated Documentation

### README Files
- Updated [README.md](README.md) with new features
- Enhanced [chatbot-ui/README.md](chatbot-ui/chatbot-ui/README.md) with detailed instructions
- Added documentation for all new utilities

## 5. Maven Integration

### New Executions in pom.xml
- `clear-chat-history`: Clears chat history from database
- All utilities can be run with Maven commands

## Usage Examples

### Clear Chat History
```
cd chatbot-ui\chatbot-ui
.\clear-chat.bat
```

### Create Desktop Shortcut
```
cd chatbot-ui\chatbot-ui
.\install-shortcut.bat
```

### Launch Application
1. Double-click [ChatbotLauncher.bat](chatbot-ui/chatbot-ui/ChatbotLauncher.bat)
2. Or double-click the "Chatbot" desktop icon (after running install-shortcut.bat)

## Benefits

1. **Easy Chat Management**: Clear chat history with a single click
2. **Convenient Launch**: Multiple ways to start the application
3. **Desktop Integration**: Professional desktop shortcut
4. **No Technical Knowledge Required**: Simple double-click operation
5. **Error Handling**: Clear error messages and troubleshooting guidance

## Requirements

All features work with the existing setup:
- Java 23
- Maven (included in distribution)
- MySQL database
- OpenAI API key

The application will automatically handle environment setup when launched through the provided scripts.