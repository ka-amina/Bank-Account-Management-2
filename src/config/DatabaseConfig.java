package config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final Dotenv dotenv = Dotenv.load();
    private static final DatabaseConfig INSTANCE = new DatabaseConfig();
    public static final String SERVER_NAME = dotenv.get("DB_HOST");
    public static final String DB_NAME = dotenv.get("DB_NAME");
    public static final String PORT_NUMBER = dotenv.get("DB_PORT");
    public static final String USERNAME = dotenv.get("DB_USER");
    public static final String PASSWORD = dotenv.get("DB_PASSWORD");
    public static Connection connection = null;

    private DatabaseConfig() {
    }

    public static DatabaseConfig getInstance() {
        return INSTANCE;
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if (connection == null) {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    "jdbc:postgresql://" + SERVER_NAME + ":" + PORT_NUMBER + "/" + DB_NAME, USERNAME, PASSWORD
            );
//            System.out.println("worked");
        }
        return connection;
    }

    public void disconect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        connection = null;
    }
}
