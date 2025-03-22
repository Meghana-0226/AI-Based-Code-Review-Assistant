import java.sql.*;
import java.util.Scanner;

public class EventFeedbackSystem {
    private static final String DB_URL = "jdbc:sqlite:feedback.db";

    public static void main(String[] args) {
        createDatabase();
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nEvent Feedback System");
            System.out.println("1. Submit Feedback");
            System.out.println("2. View Feedback");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    submitFeedback(scanner);
                    break;
                case 2:
                    viewFeedback();
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    private static void createDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS feedback (id INTEGER PRIMARY KEY AUTOINCREMENT, event_name TEXT, rating INTEGER, comments TEXT)";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void submitFeedback(Scanner scanner) {
        System.out.print("Enter event name: ");
        String eventName = scanner.nextLine();
        System.out.print("Enter rating (1-5): ");
        int rating = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter comments: ");
        String comments = scanner.nextLine();
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO feedback (event_name, rating, comments) VALUES (?, ?, ?)");) {
            pstmt.setString(1, eventName);
            pstmt.setInt(2, rating);
            pstmt.setString(3, comments);
            pstmt.executeUpdate();
            System.out.println("Feedback submitted successfully!");
        } catch (SQLException e) {
            System.out.println("Error saving feedback: " + e.getMessage());
        }
    }

    private static void viewFeedback() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM feedback")) {
            
            System.out.println("\nFeedback List:");
            while (rs.next()) {
                System.out.println("Event: " + rs.getString("event_name") + ", Rating: " + rs.getInt("rating") + ", Comments: " + rs.getString("comments"));
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving feedback: " + e.getMessage());
        }
    }
}
