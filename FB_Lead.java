package FBlead;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class FacebookLeadCollector {

    public static void main(String[] args) {
        
       

        String apiUrl = "https://graph.facebook.com/v13.0/1982377672132108/leads?access_token=EAAFK3cm8CX8BO6uOS4hFmwMTDJyS2dasZBg1kar78KOjBwZAf6x8yObbTjnjZAna87UgnZCJjNZALz80cc7vnfY53ZBhyR1aIXzyECrFShRsMhkYkkCsCdZC4ZBSC8AI1bLuGIfckxZAd5ZBkF575CESxoFi0w36qEBnJZATmZAf7KsoXSyydIVnOOvZA5Bx9MjDe76H7CHTxrEPGFvieKjXbfZBP9KJkUQwkZD";


        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

    
            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            
            String[][] leadDataList = {
                {"prathi", "sivaprathi405@gmail.com", "8870440000"},
                {"prabha", "prabhabca2023@gmail.com", "9876500000"}
            };


            saveLeadsToDatabase(leadDataList);

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  
    private static void saveLeadsToDatabase(String[][] leadDataList) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
           
            String dbURL = "jdbc:mysql://localhost:3306/your_database";
            String username = "root";
            String password = "prathi_prathi";
            connection = DriverManager.getConnection(dbURL, username, password);

         
            String insertQuery = "INSERT INTO leads (name, email, phone) VALUES (?, ?, ?)";
            preparedStatement = connection.prepareStatement(insertQuery);

          
            for (String[] leadData : leadDataList) {
                preparedStatement.setString(1, leadData[0]); 
                preparedStatement.setString(2, leadData[1]); 
                preparedStatement.setString(3, leadData[2]);
                preparedStatement.executeUpdate();
            }

            System.out.println("Leads successfully saved to MySQL database.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
     
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
