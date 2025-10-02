package repositories;

import config.DatabaseConfig;
import entities.Account;
import enums.AccountType;

import java.math.BigDecimal;
import java.sql.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public List<Account> findAll() {
        String query = " select * from accounts";
        List<Account> list = new ArrayList<>();
        try (PreparedStatement statment = connection.prepareStatement(query)) {
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

    @Override
    public boolean deactivate(String accountNumber) {
        String sql = " update accounts set isActive = false where account_number = ? and isActive = true ";
        try (PreparedStatement statment = connection.prepareStatement(sql)) {
            statment.setString(1, accountNumber);
            return statment.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error closing account: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<UUID> findIdByNumber(String accountNumber) {
        String query = "select id from accounts where account_number = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountNumber);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return Optional.of((UUID) result.getObject("id"));
            }
        } catch (SQLException e) {
            System.out.println("Error finding account by number: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean deposit(String accountNumber, BigDecimal amount) {
        String sql = "update accounts set balance = balance + ?  where account_number = ? and isActive = true ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, amount);
            statement.setString(2, accountNumber);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error depositing: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean withdraw(String accountNumber, BigDecimal amount) {
        String sql = " update accounts set balance = balance - ? where account_number = ? and isActive = true and balance >= ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, amount);
            ps.setString(2, accountNumber);
            ps.setBigDecimal(3, amount);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error withdrawing: " + e.getMessage());
        }
        return false;
    }
}
