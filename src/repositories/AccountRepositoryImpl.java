package repositories;

import config.DatabaseConfig;
import entities.Account;
import enums.AccountType;

import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountRepositoryImpl implements AccoutRepository {
    private Connection connection;

    public AccountRepositoryImpl() {
        try {
            this.connection = DatabaseConfig.getInstance().getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("err connecting to database .");
        }
    }

    @Override
    public boolean createAccount(Account account) {
        String sql = "INSERT INTO accounts(account_number, type, balance , client_id, created_by) " +
                "VALUES (?,?,?,?,?) RETURNING id";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.getAccountNumber());
            statement.setObject(2, account.getType().name(), Types.OTHER);
            statement.setBigDecimal(3, account.getBalance());
            statement.setObject(4, account.getClientId());
            statement.setInt(5, account.getCreatedBy());

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                account.setAccountId((UUID) rs.getObject("id"));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Account> findByCreatedBy(int userId) {
        String query = """
                SELECT *
                FROM accounts
                WHERE created_by = ?
                """;
        List<Account> list = new ArrayList<>();
        try (PreparedStatement statment = connection.prepareStatement(query)) {
            statment.setInt(1, userId);
            ResultSet result = statment.executeQuery();
            while (result.next()) {
                Account a = new Account();
                a.setAccountId((UUID) result.getObject("id"));
                a.setAccountNumber(result.getString("account_number"));
                a.setType(AccountType.valueOf(result.getString("type")));
                a.setBalance(result.getBigDecimal("balance"));
                a.setActive(result.getBoolean("isActive"));
                a.setClientId((UUID) result.getObject("client_id"));
                a.setCreatedBy(result.getInt("created_by"));
                list.add(a);
            }
        } catch (SQLException e) {
            System.out.println("Error listing accounts: " + e.getMessage());
        }
        return list;
    }
}
