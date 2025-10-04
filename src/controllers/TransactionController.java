package controllers;

import entities.User;
import handlers.TransactionHandler;

import java.util.UUID;

public class TransactionController {
    private final TransactionHandler transactionHandler = new TransactionHandler();

    public void deposit() {
        transactionHandler.deposit();
    }

    public void withdraw() {
        transactionHandler.withdraw();
    }

    public void transfer(UUID currentClientId) {
        transactionHandler.transfer(currentClientId);
    }

    public void showTransactionHistory() {
        transactionHandler.showTransactionHistory();
    }

    public void showPendingTransfers() {
        transactionHandler.showPendingTransfers();
    }

    public void approveTransfer() {
        transactionHandler.approveTransfer();
    }
}