# Running the Chatbot Application

## Prerequisites

1. Maven (included in this distribution)
2. Java 23+ (included in this distribution)
3. MySQL database server running locally

## Setting up the Environment

Before running the application for the first time, you need to set up the database:

1. Make sure MySQL is running on your system
2. Run the database setup script:
   ```
   .\setup-db.bat
   ```

## Running the Application

To run the chatbot application:

1. Make sure you're in the `chatbot-ui\chatbot-ui` directory
2. Run the application using Maven:
   ```
   .\mvn.bat javafx:run
   ```

Alternatively, you can use the provided run script:
```
.\run.bat
```

## Troubleshooting

### If you get "mvn is not recognized" error

Run the environment setup script first:
```
..\..\setenv.bat
```

### If the application fails to start

1. Check that MySQL is running
2. Verify database credentials in [ChatService.java](src/main/java/com/farzan/ChatService.java)
3. Ensure your OpenAI API key is valid and set in [ChatService.java](src/main/java/com/farzan/ChatService.java)

### If you see JavaFX errors

Make sure you're using Java 23 or higher, which includes JavaFX modules.