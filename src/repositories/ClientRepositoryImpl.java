package repositories;

import config.DatabaseConfig;
import entities.Client;

import java.sql.*;
import java.util.UUID;

public class ClientRepositoryImpl implements ClientRepository  {
    private Connection connection;

    public ClientRepositoryImpl(){
        try{
            this.connection= DatabaseConfig.getInstance().getConnection();
        }catch (SQLException|ClassNotFoundException e){
            System.out.println("err connecting to database .");
        }
    }

    @Override
    public boolean createClient(Client client){
        String query = "insert into clients(first_name,last_name,cin,phone_number,address,email,created_by) values(?,?,?,?,?,?,?) returning id";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, client.getFirstName());
            statement.setString(2,client.getLastName());
            statement.setString(3, client.getCin());
            statement.setString(4,client.getPhoneNumber());
            statement.setString(5,client.getAddress());
            statement.setString(6,client.getEmail());
            statement.setInt(7, client.getCreatedBy());
            ResultSet result = statement.executeQuery();
            if(result.next()){
                UUID generateId= UUID.fromString(result.getString("id"));
                client.setClientId(generateId);
                return true;

            }
        }catch (SQLException e){
            System.out.println("error adding new client"+e.getMessage());
        }
        return false;

    }
}
