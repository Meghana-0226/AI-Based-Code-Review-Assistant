import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class AICodeReviewAssistant {
    private static final String API_KEY = "your_openai_api_key";
    private static final String API_URL = "https://api.openai.com/v1/completions";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Java code (type 'END' on a new line to finish):");
        StringBuilder codeInput = new StringBuilder();
        
        while (true) {
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("END")) {
                break;
            }
            codeInput.append(line).append("\n");
        }
        
        scanner.close();
        String review = getAIReview(codeInput.toString());
        System.out.println("\nAI Code Review:\n" + review);
    }

    private static String getAIReview(String code) {
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            String jsonInput = "{\"model\": \"gpt-4\", \"prompt\": \"Review the following Java code and provide suggestions: " + code.replace("\"", "\\\"") + "\", \"max_tokens\": 200}";
            OutputStream os = conn.getOutputStream();
            os.write(jsonInput.getBytes());
            os.flush();
            os.close();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
