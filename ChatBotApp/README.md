private static final String DB_USER = "root";
private static final String DB_PASSWORD = "491811";

// External AI API Key
private static final String API_KEY = "YOUR_API_KEY_HERE"; // Replace with your actual OpenAI API key
```

### 3. Add MySQL JDBC Driver

Download the MySQL JDBC driver (mysql-connector-java-x.x.x.jar) and place it in the `lib` folder.

## Compiling and Running

### Option 1: Using Command Line

#### Compile the Application


```bash
javac -cp "lib/*" -d out src/*.java
```

#### Run the Application

```bash
java -cp "out;lib/*" ChatWindow
```

On Unix-like systems, use colons instead of semicolons:
```bash
java -cp "out:lib/*" ChatWindow
```

### Option 2: Using Batch Files (Windows)

1. Double-click `compile.bat` to compile the application
2. Double-click `run.bat` to run the application

## Usage

1. Type your message in the input field at the bottom
2. Press Enter or click "Send" to submit
3. The bot's response will appear in the conversation area
4. All messages are logged to the MySQL database

## Troubleshooting

### Database Error: "Failed to log message to database"

1. **Check MySQL Server**: Ensure MySQL is running on your system
2. **Verify Connection Details**: Check that the database name, username, and password in `ChatWindow.java` are correct
3. **JDBC Driver**: Make sure the MySQL JDBC driver is in the `lib` folder
4. **Database Permissions**: Ensure your MySQL user has permissions to create tables and insert data
5. **Firewall**: Check that no firewall is blocking connections to MySQL (default port 3306)

### API Error: "API request failed with HTTP code: 400"

1. **Check API Key**: Verify that your API key is valid and correctly set in `ChatWindow.java`
2. **API Key Permissions**: Ensure your API key has the necessary permissions
3. **Internet Connection**: Verify you have a stable internet connection
4. **Rate Limits**: Check if you've exceeded the API's rate limits

### General Troubleshooting Tips

1. **View Detailed Error Messages**: The application now shows more detailed error information to help diagnose issues
2. **Check Console Output**: Look at the console/terminal where you started the application for additional error details
3. **Test Database Connection**: Try connecting to your MySQL database using a MySQL client to verify credentials
4. **Test API Key**: Try your API key with a direct curl request to verify it works with your AI service.

## Modern JavaFX Version

For a more modern UI experience, check out the JavaFX version in the [chatbot-ui](../chatbot-ui/chatbot-ui) directory which includes:
- Improved user interface with dark/light themes
- Better message display with animations
- Enhanced history viewing
- Same backend integration with AI and database

## Notes

- The application does not display any mention of "OpenAI" or "ChatGPT" in the UI
- Error messages are shown in dialog boxes
- All database operations and API calls are performed asynchronously to keep the UI responsive