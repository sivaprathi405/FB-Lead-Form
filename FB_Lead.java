package FBlead;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class FacebookLeadCollector {

    public static void main(String[] args) {
        // Replace these values with your own
       

        // Make a GET request to retrieve leads for a specific form
        String apiUrl = "https://graph.facebook.com/v13.0/1982377672132108/leads?access_token=EAAFK3cm8CX8BO6uOS4hFmwMTDJyS2dasZBg1kar78KOjBwZAf6x8yObbTjnjZAna87UgnZCJjNZALz80cc7vnfY53ZBhyR1aIXzyECrFShRsMhkYkkCsCdZC4ZBSC8AI1bLuGIfckxZAd5ZBkF575CESxoFi0w36qEBnJZATmZAf7KsoXSyydIVnOOvZA5Bx9MjDe76H7CHTxrEPGFvieKjXbfZBP9KJkUQwkZD";


        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            // Process the response as a simple two-dimensional array
            String[][] leadDataList = {
                {"John Doe", "john@example.com", "1234567890"},
                {"Jane Smith", "jane@example.com", "9876543210"}
            };

            // Save lead data to MySQL database
            saveLeadsToDatabase(leadDataList);

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to save lead data to MySQL database
    private static void saveLeadsToDatabase(String[][] leadDataList) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Establish database connection
            String dbURL = "jdbc:mysql://localhost:3306/your_database";
            String username = "your_username";
            String password = "your_password";
            connection = DriverManager.getConnection(dbURL, username, password);

            // Prepare SQL INSERT statement
            String insertQuery = "INSERT INTO leads (name, email, phone) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);

            // Insert each lead data into the database
            for (String[] leadData : leadDataList) {
                preparedStatement.setString(1, leadData[0]); // Name
                preparedStatement.setString(2, leadData[1]); // Email
                preparedStatement.setString(3, leadData[2]); // Phone
                preparedStatement.executeUpdate();
            }

            System.out.println("Leads successfully saved to MySQL database.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}