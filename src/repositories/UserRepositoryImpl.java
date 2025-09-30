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
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                int id = Integer.parseInt(result.getString("id"));
                String name = result.getString("name");
                String password = result.getString("password");
                UserRole role = UserRole.valueOf(result.getString("role"));
                User user = new User(id, name, email, password, role);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.out.println("error fetching user: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById(int id) {
        String query = "select * from users where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                User user = new User(
                        result.getInt("id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("password"),
                        UserRole.valueOf(result.getString("role"))
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            System.out.println("Error finding user by id: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean updatePassword(int userId, String newPassword) {
        String query = "update users set password = ? where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newPassword);
            statement.setInt(2, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error updating password: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean updateProfile(int userId, String newName, String newEmail) {
        String sql = "update users set name = ?, email = ? where id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setString(2, newEmail);
            statement.setInt(3, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error updating profile: " + e.getMessage());
        }
        return false;
    }
}
