import config.DatabaseConfig;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConfig dbConfig = DatabaseConfig.getInstance();
            Connection conn = dbConfig.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("Connected");


                dbConfig.disconect();

                System.out.println(" Disconnected.");
            }

        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }
}
