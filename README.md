# Chatbot Application

This repository contains a complete chatbot application with both a legacy Swing version and a modern JavaFX frontend, both integrated with OpenAI's API and MySQL database.

## Project Structure

```
CHATBOT FINAL/
├── ChatBotApp/                 # Legacy Swing version
│   ├── src/                   # Source code
│   ├── lib/                   # Libraries (JDBC driver)
│   ├── resources/             # Database schema
│   ├── compile.bat            # Compilation script
│   ├── run.bat                # Run script
│   └── README.md              # Documentation for Swing version
│
├── chatbot-ui/                # Modern JavaFX version
│   └── chatbot-ui/           # Main UI project
│       ├── src/              # Source code
│       ├── pom.xml           # Maven configuration
│       ├── run.bat           # Run script
│       ├── setup-db.bat      # Database setup script
│       └── README.md         # Documentation for JavaFX version
│
└── README.md                 # This file
```

## Features

Both versions include:
- Integration with OpenAI's GPT-3.5-turbo model
- Persistent message logging to MySQL database
- Real-time conversation interface
- Error handling for API and database issues

The JavaFX version adds:
- Modern, responsive UI with animations
- Dark/light theme toggle
- Enhanced message display
- Improved history viewing
- Single-click application launch
- Chat history management

## Prerequisites

1. **Java 23** (for JavaFX version) or **Java 8+** (for Swing version)
2. **Maven** (for JavaFX version)
3. **MySQL** database server
4. **OpenAI API key**

## Setup Instructions

### Database Setup

1. Ensure MySQL is installed and running
2. Update database credentials in the source files if needed:
   - Swing version: [ChatWindow.java](ChatBotApp/src/ChatWindow.java)
   - JavaFX version: [ChatService.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/ChatService.java) and [DatabaseSetup.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/DatabaseSetup.java)

3. For the JavaFX version, run the database setup utility:
   ```bash
   cd chatbot-ui/chatbot-ui
   ./setup-db.bat
   ```

### OpenAI API Key

1. Get your OpenAI API key from https://platform.openai.com/api-keys
2. Update the API key in the source files:
   - Swing version: [ChatWindow.java](ChatBotApp/src/ChatWindow.java)
   - JavaFX version: [ChatService.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/ChatService.java)

## Running the Applications

### JavaFX Version (Recommended)

```bash
cd chatbot-ui/chatbot-ui
./run.bat
```

Or manually:
```bash
cd chatbot-ui/chatbot-ui
mvn clean compile
mvn javafx:run
```

### Single-Click Launch (JavaFX Version)

1. **Create Desktop Shortcut**:
   ```bash
   cd chatbot-ui/chatbot-ui
   ./install-shortcut.bat
   ```
   
2. **Launch Application**: Double-click the "Chatbot" icon on your desktop

### Managing Chat History

To clear all chat history from the database:
```bash
cd chatbot-ui/chatbot-ui
./clear-chat.bat
```

## Development

### JavaFX Version

The JavaFX version uses Maven for dependency management. Key classes:

- [MainApp.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/MainApp.java) - Application entry point
- [MainController.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/MainController.java) - UI controller with AI integration
- [ChatService.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/ChatService.java) - Service layer for AI and database
- [DatabaseSetup.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/DatabaseSetup.java) - Database initialization utility
- [ClearChatHistory.java](chatbot-ui/chatbot-ui/src/main/java/com/farzan/ClearChatHistory.java) - Chat history clearing utility

### Swing Version

The Swing version is a standalone application with all functionality in:

- [ChatWindow.java](ChatBotApp/src/ChatWindow.java) - Main application window
- [AiServiceClient.java](ChatBotApp/src/AiServiceClient.java) - AI service client
- [ChatLogger.java](ChatBotApp/src/ChatLogger.java) - Database logging

## Troubleshooting

See individual README files for version-specific troubleshooting:
- [Swing Version README](ChatBotApp/README.md)
- [JavaFX Version README](chatbot-ui/chatbot-ui/README.md)