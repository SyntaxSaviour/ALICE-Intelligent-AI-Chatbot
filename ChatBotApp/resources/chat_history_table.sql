-- SQL script to create the chat_history table

CREATE DATABASE IF NOT EXISTS chatbot_db;

USE chatbot_db;

CREATE TABLE IF NOT EXISTS chat_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sender VARCHAR(50),
    message TEXT,
    ts DATETIME
);

-- Sample query to view chat history
SELECT * FROM chat_history ORDER BY ts DESC;