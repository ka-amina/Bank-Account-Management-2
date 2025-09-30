package services;

import entities.Transaction;
import repositories.TransactionRepository;
import repositories.TransactionRepositoryImpl;

public class TransactionService {
    private final TransactionRepository transactionRepository = new TransactionRepositoryImpl();

    public boolean add(Transaction t) {
        return transactionRepository.add(t);
    }
}