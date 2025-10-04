package repositories;

import entities.Transaction;
import enums.TransactionStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository {
    boolean add(Transaction transaction);

    List<Transaction> findByAccountId(UUID accountId);

    List<Transaction> findPendingTransactions();

    boolean updateStatus(UUID transactionId, TransactionStatus status);

    Optional<Transaction> findById(UUID transactionId);
}