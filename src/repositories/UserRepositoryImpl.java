package repositories;


import config.DatabaseConfig;
import entities.User;
import enums.UserRole;

import java.sql.*;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {
    private Connection connection;

    public UserRepositoryImpl() {
        try {
            this.connection = DatabaseConfig.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("error during connecting to database : " + e.getMessage());
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String query = "select * from users where email =? ";
        try(PreparedStatement statement= connection.prepareStatement(query)){
            statement.setString(1,email);
            ResultSet result=statement.executeQuery();
            if (result.next()) {
                int id = Integer.parseInt(result.getString("id"));
                String name = result.getString("name");
                String password = result.getString("password");
                UserRole role = UserRole.valueOf(result.getString("role"));
                User user = new User(id, name, email, password, role);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.out.println("error fetching user: "+ e.getMessage());
        }
        return Optional.empty();
    }
}
