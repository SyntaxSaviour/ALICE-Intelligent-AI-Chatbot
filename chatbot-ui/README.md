# Chatbot UI - Integrated Version

This is the integrated frontend for the Chatbot application that connects to the OpenAI API and MySQL database.

## Project Structure

```
chatbot-ui/
├── src/
│   ├── main/
│   │   ├── java/com/farzan/
│   │   │   ├── MainApp.java          # Main application entry point
│   │   │   ├── MainController.java    # UI controller with AI integration
│   │   │   ├── ChatService.java      # Service layer for AI and database
│   │   │   ├── DatabaseSetup.java    # Database initialization utility
│   │   │   └── ClearChatHistory.java # Chat history clearing utility
│   │   └── resources/
│   │       ├── ui/main.fxml          # UI layout
│   │       ├── css/theme.css         # Styling
│   │       └── fonts/                # Font files
├── pom.xml                           # Maven configuration
└── README.md                         # This file
```

## Prerequisites


1. **Java 25** or higher
2. **Maven** for dependency management
3. **MySQL** database server
4. **OpenAI API key**

## Setup Instructions

### 1. Database Setup

1. Ensure MySQL is installed and running on your system
2. Update the database credentials in [ChatService.java](src/main/java/com/farzan/ChatService.java) and [DatabaseSetup.java](src/main/java/com/farzan/DatabaseSetup.java) if needed:
   ```java
   private static final String DB_HOST = "localhost";
   private static final int DB_PORT = 3306;
   private static final String DB_NAME = "chatbot_db";
   private static final String DB_USER = "root";
   private static final String DB_PASSWORD = "your_password_here";
   ```

3. Run the database setup utility:
   ```bash
   mvn compile exec:java -Dexec.mainClass="com.farzan.DatabaseSetup"
   ```

### 2. OpenAI API Key

1. Get your OpenAI API key from https://platform.openai.com/api-keys
2. Update the API key in [ChatService.java](src/main/java/com/farzan/ChatService.java):
   ```java
   private static final String API_KEY = "your_api_key_here";
   ```

## Running the Application

### Using Maven

```bash
# Compile the project
mvn clean compile

# Run the application
mvn javafx:run
```

### Using Batch Files (Windows)

1. **Normal Launch**: Double-click [launch-chatbot.bat](launch-chatbot.bat) to run with console output
2. **Silent Launch**: Double-click [ChatbotLauncher.bat](ChatbotLauncher.bat) to run without console window
3. **Run Script**: Double-click [run.bat](run.bat) for the standard run script

## Managing Chat History

### Clearing Chat History

To clear all chat history from the database:

1. **Using Maven**:
   ```bash
   mvn compile exec:java@clear-chat-history
   ```

2. **Using Batch File**: Double-click [clear-chat.bat](clear-chat.bat)

### Viewing Chat History

The chat history is automatically loaded when you open the "History" tab in the application.

## Features

- **Real-time Chat**: Communicate with the AI assistant
- **Message History**: View chat history from previous sessions
- **Database Persistence**: All messages are saved to MySQL database
- **Modern UI**: Clean, responsive interface with dark/light themes
- **Single-Click Launch**: Run the application with a double-click

## Troubleshooting

### Database Connection Issues

1. Ensure MySQL is running:
   ```bash
   # On Windows
   net start mysql
   
   # On macOS/Linux
   sudo service mysql start
   ```

2. Verify database credentials in the Java files

3. Check that the MySQL JDBC driver is in your classpath

### API Key Issues

1. Ensure your OpenAI API key is valid and has credits
2. Check that you haven't exceeded rate limits
3. Verify the API key is correctly set in [ChatService.java](src/main/java/com/farzan/ChatService.java)

## Dependencies

This project uses the following dependencies (automatically managed by Maven):

- JavaFX 25 (Controls and FXML)
- MySQL Connector/J 9.4.0

## License

This project is for educational purposes only.