package controllers;

import entities.User;
import handlers.TransactionHandler;

public class TransactionController {
    private final TransactionHandler transactionHandler = new TransactionHandler();

    public void deposit() {
        transactionHandler.deposit();
    }

    public void withdraw() {
        transactionHandler.withdraw();
    }
}