package repositories;

import entities.Transaction;

public interface TransactionRepository {
    boolean add(Transaction transaction);
}