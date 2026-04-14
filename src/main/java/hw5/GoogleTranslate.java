package hw5;

// JSON array (list)
import org.json.JSONArray;
// JSON object (key-value)
import org.json.JSONObject;

// Reads text input
import java.io.BufferedReader;
// Converts bytes to text
import java.io.InputStreamReader;
// Sends data out
import java.io.OutputStream;
// Makes HTTP requests
import java.net.HttpURLConnection;
// Web address
import java.net.URL;
// Encodes URL text
import java.net.URLEncoder;
// UTF-8 text format
import java.nio.charset.StandardCharsets;


public class GoogleTranslate {

    public static String translateText(String apiKey, String plaintext, String targetLanguage) {
        try { // Build the request URL with API key
            String urlString = "https://translation.googleapis.com/language/translate/v2?key="
                    + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);

            // Create URL object
            URL url = new URL(urlString);
            // Open HTTP connection to the API endpoint
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method to POST
            connection.setRequestMethod("POST");
            // Set content type to JSON
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            // Enable sending data in request body
            connection.setDoOutput(true);

            String parameters = "q=" + URLEncoder.encode(plaintext, StandardCharsets.UTF_8)
                    + "&target=" + URLEncoder.encode(targetLanguage, StandardCharsets.UTF_8);

            // Send JSON data to API
            try (OutputStream os = connection.getOutputStream()) {
                // Convert string to bytes using UTF-8 encoding
                byte[] input = parameters.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Read response from API (input stream)
            BufferedReader reader;
            if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8));
            }

            // Store response text
            StringBuilder response = new StringBuilder();
            String line;

            // Read response line by line
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            if (connection.getResponseCode() < 200 || connection.getResponseCode() >= 300) {
                throw new RuntimeException("API Error: " + response);
            }

            JSONObject jsonObject = new JSONObject(response.toString());
            JSONArray translations = jsonObject.getJSONObject("data").getJSONArray("translations");

            return translations.getJSONObject(0).getString("translatedText");

        } catch (Exception e) {
            throw new RuntimeException("Translation failed: " + e.getMessage(), e);
        }
    }
}