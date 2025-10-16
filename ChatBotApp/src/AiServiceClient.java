import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Client for communicating with the external AI API.
 */
public class AiServiceClient {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";
    private final String apiKey;
    
    /**
     * Constructor to initialize with API key
     */
    public AiServiceClient(String apiKey) {
        this.apiKey = apiKey;
    }
    
    /**
     * Get response from the AI service for a user message
     */
    public String getChatResponse(String userMessage) throws Exception {
        // Create JSON request manually with proper escaping
        String escapedMessage = escapeJson(userMessage);
        String jsonInputString = "{\"model\":\"" + MODEL + "\",\"messages\":[{\"role\":\"user\",\"content\":\"" + escapedMessage + "\"}],\"temperature\":0.7}";
        
        // Create HTTP connection
        URL url = new URL(API_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + apiKey);
        con.setDoOutput(true);
        
        // Send request
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        // Check response code
        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            // Read error response
            StringBuilder errorResponse = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getErrorStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    errorResponse.append(responseLine.trim());
                }
            } catch (Exception e) {
                // Ignore error reading error
            }
            throw new RuntimeException("API request failed with HTTP code: " + responseCode + 
                                     ". Error: " + errorResponse.toString());
        }
        
        // Read response
        StringBuilder response = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
        
        // Parse JSON response and extract the reply text
        return parseResponse(response.toString());
    }
    
    /**
     * Parse the JSON response from the AI API and extract the reply text
     */
    private String parseResponse(String jsonResponse) throws Exception {
        // Check for errors first
        if (jsonResponse.contains("\"error\"")) {
            // Simple extraction of error message
            Pattern errorPattern = Pattern.compile("(\"message\":\\s*\"([^\"]*)\")");
            Matcher errorMatcher = errorPattern.matcher(jsonResponse);
            if (errorMatcher.find()) {
                String errorMsg = errorMatcher.group(2);
                throw new RuntimeException("API Error: " + errorMsg);
            } else {
                throw new RuntimeException("Unknown API error");
            }
        }
        
        // Extract the content from the response
        // Looking for pattern: "content":"text here"
        Pattern contentPattern = Pattern.compile("(\"content\":\\s*\"([^\"]*)\")");
        Matcher contentMatcher = contentPattern.matcher(jsonResponse);
        
        if (contentMatcher.find()) {
            String content = contentMatcher.group(2);
            // Unescape common JSON escape sequences
            content = content.replace("\\n", "\n")
                             .replace("\\\"", "\"")
                             .replace("\\\\", "\\");
            return content;
        } else {
            throw new RuntimeException("Could not extract response content from API. Response: " + jsonResponse);
        }
    }
    
    /**
     * Escape special characters in JSON strings
     */
    public String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}