package repositories;

import config.DatabaseConfig;
import entities.Transaction;
import java.sql.*;
import java.util.UUID;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final Connection conn;

    public TransactionRepositoryImpl() {
        try { conn = DatabaseConfig.getInstance().getConnection(); }
        catch (Exception e) { throw new RuntimeException(e); }
    }

    @Override
    public boolean add(Transaction transaction) {
        String query = " insert into transactions(amount, type, sender_account_id, receiver_account_id) values (?, ?, ?, ?) RETURNING id ";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setBigDecimal(1, transaction.getAmount());
            statement.setObject(2, transaction.getType().name(),Types.OTHER);
            statement.setObject(3, transaction.getSenderAccountId());
            statement.setObject(4, transaction.getReceiverAccountId());
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
}