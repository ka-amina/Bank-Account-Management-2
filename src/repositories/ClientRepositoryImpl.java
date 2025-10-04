package repositories;

import config.DatabaseConfig;
import entities.Client;

import java.sql.*;
import java.util.*;
import java.util.Optional;
import java.util.UUID;

public class ClientRepositoryImpl implements ClientRepository {
    private Connection connection;

    public ClientRepositoryImpl() {
        try {
            this.connection = DatabaseConfig.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("err connecting to database .");
        }
    }

    @Override
    public boolean createClient(Client client) {
        String query = "insert into clients(first_name,last_name,cin,phone_number,address,email,created_by) values(?,?,?,?,?,?,?) returning id";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, client.getFirstName());
            statement.setString(2, client.getLastName());
            statement.setString(3, client.getCin());
            statement.setString(4, client.getPhoneNumber());
            statement.setString(5, client.getAddress());
            statement.setString(6, client.getEmail());
            statement.setInt(7, client.getCreatedBy());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                UUID generateId = UUID.fromString(result.getString("id"));
                client.setClientId(generateId);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("error adding new client" + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<UUID> findIdByCin(String cin) {
        String query = "SELECT id FROM clients WHERE cin = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, cin);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of((UUID) rs.getObject("id"));
            }
        } catch (SQLException e) {
            System.out.println("Error finding client by CIN: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Optional<UUID> findClientIdByUserId(int userId) {
        String query = "SELECT id FROM clients WHERE created_by = ? LIMIT 1";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return Optional.of((UUID) rs.getObject("id"));
            }
        } catch (SQLException e) {
            System.out.println("Error finding client by user ID: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        String query = "SELECT * FROM clients ORDER BY first_name, last_name";
        List<Client> clients = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Client c = new Client(
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("cin"),
                        rs.getString("phone_number"),
                        rs.getString("address"),
                        rs.getInt("created_by")
                );
                c.setClientId((UUID) rs.getObject("id"));
                clients.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error listing clients: " + e.getMessage());
        }
        return clients;
    }
}