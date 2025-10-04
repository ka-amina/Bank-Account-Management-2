package repositories;

import config.DatabaseConfig;
import entities.Transaction;
import enums.TransactionStatus;
import enums.TransactionType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final Connection conn;

    public TransactionRepositoryImpl() {
        try {
            conn = DatabaseConfig.getInstance().getConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean add(Transaction transaction) {
        String query = " insert into transactions(amount, type, sender_account_id, receiver_account_id) values (?, ?, ?, ?) RETURNING id ";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setBigDecimal(1, transaction.getAmount());
            statement.setObject(2, transaction.getType().name(),Types.OTHER);
            statement.setObject(3, transaction.getSenderAccountId());
            statement.setObject(4, transaction.getReceiverAccountId());
            statement.setString(5, transaction.getStatus().name());
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                transaction.setId((UUID) result.getObject("id"));
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
        return false;
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        String query = "SELECT * FROM transactions " +
                "WHERE sender_account_id = ? OR receiver_account_id = ? " +
                "ORDER BY timestamp DESC";
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setObject(1, accountId);
            statement.setObject(2, accountId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getBigDecimal("amount"),
                        TransactionType.valueOf(rs.getString("type")),
                        (UUID) rs.getObject("sender_account_id"),
                        (UUID) rs.getObject("receiver_account_id")
                );
                t.setId((UUID) rs.getObject("id"));
                t.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                t.setStatus(TransactionStatus.valueOf(rs.getString("status")));
                transactions.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error finding transactions: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public List<Transaction> findPendingTransactions() {
        String query = "SELECT * FROM transactions WHERE status = 'PENDING' ORDER BY timestamp";
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction(
                        rs.getBigDecimal("amount"),
                        TransactionType.valueOf(rs.getString("type")),
                        (UUID) rs.getObject("sender_account_id"),
                        (UUID) rs.getObject("receiver_account_id")
                );
                t.setId((UUID) rs.getObject("id"));
                t.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                t.setStatus(TransactionStatus.valueOf(rs.getString("status")));
                transactions.add(t);
            }
        } catch (SQLException e) {
            System.out.println("Error finding pending transactions: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public boolean updateStatus(UUID transactionId, TransactionStatus status) {
        String query = "UPDATE transactions SET status = ? WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, status.name());
            statement.setObject(2, transactionId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            System.out.println("Error updating transaction status: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Optional<Transaction> findById(UUID transactionId) {
        String query = "SELECT * FROM transactions WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setObject(1, transactionId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Transaction t = new Transaction(
                        rs.getBigDecimal("amount"),
                        TransactionType.valueOf(rs.getString("type")),
                        (UUID) rs.getObject("sender_account_id"),
                        (UUID) rs.getObject("receiver_account_id")
                );
                t.setId((UUID) rs.getObject("id"));
                t.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                t.setStatus(TransactionStatus.valueOf(rs.getString("status")));
                return Optional.of(t);
            }
        } catch (SQLException e) {
            System.out.println("Error finding transaction by ID: " + e.getMessage());
        }
        return Optional.empty();
    }
}