package services;

import entities.Transaction;
import enums.TransactionStatus;
import repositories.TransactionRepository;
import repositories.TransactionRepositoryImpl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepositoryImpl();

    public boolean add(Transaction t) {
        return transactionRepository.add(t);
    }

    public List<Transaction> getTransactionHistory(UUID accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public List<Transaction> getPendingTransactions() {
        return transactionRepository.findPendingTransactions();
    }

    public boolean updateStatus(UUID transactionId, TransactionStatus status) {
        return transactionRepository.updateStatus(transactionId, status);
    }

    public Optional<Transaction> findById(UUID transactionId) {
        return transactionRepository.findById(transactionId);
    }
}