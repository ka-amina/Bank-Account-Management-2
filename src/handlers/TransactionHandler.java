package handlers;

import entities.Account;
import entities.Transaction;
import enums.TransactionType;
import services.AccountService;
import services.ClientService;
import services.TransactionService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class TransactionHandler {
    private final Scanner sc = new Scanner(System.in);
    private final AccountService accountService = new AccountService();
    private final TransactionService transactionService = new TransactionService();
    private final ClientService clientService = new ClientService();

    public void deposit() {
        System.out.print("Account number to deposit into: ");
        String number = sc.nextLine().trim();

        System.out.print("Amount to deposit: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(sc.nextLine().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Amount must be a positive number.");
            return;
        }

        boolean ok = accountService.deposit(number, amount);
        if (ok) {
            System.out.println("✓ Deposit successful.");
        } else {
            System.out.println("✗ Deposit failed (account not found, not yours, or inactive).");
        }
    }

    public void withdraw() {
        System.out.print("Account number to withdraw from: ");
        String number = sc.nextLine().trim();

        System.out.print("Amount to withdraw: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(sc.nextLine().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Amount must be a positive number.");
            return;
        }

        boolean ok = accountService.withdraw(number, amount);
        if (ok) {
            System.out.println("✓ Withdrawal successful.");
        } else {
            System.out.println("✗ Withdrawal failed (account not found, not yours, inactive, or insufficient balance).");
        }
    }

    public void transfer(UUID clientId) {
        if (clientId == null) {
            System.out.println("✗ Invalid client ID.");
            return;
        }

        System.out.println("\n=== Transfer Menu ===");
        System.out.println("1. Transfer between client's own accounts (Internal)");
        System.out.println("2. Transfer to another client's account (External - requires manager approval)");
        System.out.print("Choice: ");

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        if (choice == 1) {
            transferInternal(clientId);
        } else if (choice == 2) {
            transferExternal(clientId);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    private void transferInternal(UUID currentClientId) {
        List<Account> myAccounts = accountService.getAccountsByClientId(currentClientId);

        if (myAccounts.size() < 2) {
            System.out.println("✗ This client needs at least 2 accounts to make an internal transfer.");
            return;
        }

        System.out.println("\n=== Client's Accounts ===");
        for (int i = 0; i < myAccounts.size(); i++) {
            Account acc = myAccounts.get(i);
            System.out.printf("%d. %s - Balance: %.2f MAD - Type: %s\n", i + 1, acc.getAccountNumber(), acc.getBalance(), acc.getType());
        }

        System.out.print("\nFrom account number: ");
        String fromAccount = sc.nextLine().trim();

        System.out.print("To account number: ");
        String toAccount = sc.nextLine().trim();

        if (fromAccount.equals(toAccount)) {
            System.out.println("✗ Cannot transfer to the same account.");
            return;
        }

        Optional<Account> fromAcc = accountService.findByNumber(fromAccount);
        Optional<Account> toAcc = accountService.findByNumber(toAccount);

        if (!fromAcc.isPresent() || !toAcc.isPresent()) {
            System.out.println("✗ One or both accounts not found.");
            return;
        }

        if (!fromAcc.get().getClientId().equals(currentClientId) || !toAcc.get().getClientId().equals(currentClientId)) {
            System.out.println("✗ Both accounts must belong to the specified client for internal transfer.");
            return;
        }

        System.out.print("Amount to transfer: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(sc.nextLine().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Amount must be a positive number.");
            return;
        }

        boolean success = accountService.transferInternal(fromAccount, toAccount, amount);
        if (success) {
            System.out.println("✓ Internal transfer completed successfully!");
            System.out.println("  From: " + fromAccount);
            System.out.println("  To: " + toAccount);
            System.out.println("  Amount: " + amount + " MAD");
        } else {
            System.out.println("✗ Transfer failed (insufficient funds or inactive account).");
        }
    }

    private void transferExternal(UUID currentClientId) {
        List<Account> myAccounts = accountService.getAccountsByClientId(currentClientId);

        if (myAccounts.isEmpty()) {
            System.out.println("✗ This client has no active accounts.");
            return;
        }

        System.out.println("\n=== Client's Accounts ===");
        for (int i = 0; i < myAccounts.size(); i++) {
            Account acc = myAccounts.get(i);
            System.out.printf("%d. %s - Balance: %.2f MAD - Type: %s\n", i + 1, acc.getAccountNumber(), acc.getBalance(), acc.getType());
        }

        System.out.print("\nFrom account number: ");
        String fromAccount = sc.nextLine().trim();

        System.out.print("To account number (recipient account): ");
        String toAccount = sc.nextLine().trim();

        if (fromAccount.equals(toAccount)) {
            System.out.println("✗ Cannot transfer to the same account.");
            return;
        }

        Optional<Account> fromAcc = accountService.findByNumber(fromAccount);
        Optional<Account> toAcc = accountService.findByNumber(toAccount);

        if (!fromAcc.isPresent() || !toAcc.isPresent()) {
            System.out.println("✗ One or both accounts not found.");
            return;
        }

        if (!fromAcc.get().getClientId().equals(currentClientId)) {
            System.out.println("✗ The source account must belong to the specified client.");
            return;
        }

        if (fromAcc.get().getClientId().equals(toAcc.get().getClientId())) {
            System.out.println("✗ Use internal transfer for transfers between the same client's accounts.");
            return;
        }

        System.out.print("Amount to transfer: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(sc.nextLine().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Amount must be a positive number.");
            return;
        }

        boolean success = accountService.transferExternal(fromAccount, toAccount, amount);
        if (success) {
            System.out.println("✓ External transfer request submitted successfully!");
            System.out.println("  From: " + fromAccount);
            System.out.println("  To: " + toAccount);
            System.out.println("  Amount: " + amount + " MAD");
            System.out.println("⏳ Status: PENDING - Awaiting manager approval");
        } else {
            System.out.println("✗ Transfer request failed (account not found, inactive, or insufficient funds check).");
        }
    }

    public void showTransactionHistory() {
        System.out.print("Enter account number: ");
        String accountNumber = sc.nextLine().trim();

        Optional<Account> accountOpt = accountService.findByNumber(accountNumber);
        if (!accountOpt.isPresent()) {
            System.out.println("✗ Account not found.");
            return;
        }

        Account account = accountOpt.get();
        List<Transaction> transactions = transactionService.getTransactionHistory(account.getAccountId());

        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }

        System.out.println("\n" + "=".repeat(100));
        System.out.println("TRANSACTION HISTORY - Account: " + accountNumber);
        System.out.println("=".repeat(100));
        System.out.printf("%-20s | %-15s | %-12s | %-10s | %-30s\n", "Date/Time", "Type", "Amount (MAD)", "Status", "Details");
        System.out.println("-".repeat(100));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (Transaction tx : transactions) {
            String type = tx.getType().toString();
            String amount = String.format("%.2f", tx.getAmount());
            String status = tx.getStatus().toString();
            String datetime = tx.getTimestamp().format(formatter);
            String details = getTransactionDetails(tx, account.getAccountId());

            System.out.printf("%-20s | %-15s | %-12s | %-10s | %-30s\n", datetime, type, amount, status, details);
        }
        System.out.println("=".repeat(100));
        System.out.printf("\nCurrent Balance: %.2f MAD\n", account.getBalance());
    }

    private String getTransactionDetails(Transaction tx, UUID currentAccountId) {
        TransactionType type = tx.getType();

        switch (type) {
            case DEPOSIT:
                return "Money deposited";
            case WITHDRAW:
                return "Money withdrawn";
            case TRANSFER_IN:
                return "Transfer received";
            case TRANSFER_OUT:
                return "Transfer sent";
            case TRANSFER_EXTERNAL:
                if (tx.getSenderAccountId().equals(currentAccountId)) {
                    return "External transfer (sent)";
                } else {
                    return "External transfer (received)";
                }
            case FEE:
                return "Transaction fee charged";
            case FEEINCOME:
                return "Fee income received";
            default:
                return "Other transaction";
        }
    }

    public void showPendingTransfers() {
        List<Transaction> pending = transactionService.getPendingTransactions();

        if (pending.isEmpty()) {
            System.out.println("No pending transfers.");
            return;
        }

        System.out.println("\n" + "=".repeat(120));
        System.out.println("PENDING EXTERNAL TRANSFERS (Awaiting Manager Approval)");
        System.out.println("=".repeat(120));
        System.out.printf("%-5s | %-36s | %-20s | %-12s | %-36s\n", "No.", "Transaction ID", "Date/Time", "Amount (MAD)", "From Account");
        System.out.println("-".repeat(120));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < pending.size(); i++) {
            Transaction tx = pending.get(i);
            System.out.printf("%-5d | %-36s | %-20s | %-12.2f | %-36s\n", i + 1, tx.getId().toString(), tx.getTimestamp().format(formatter), tx.getAmount(), tx.getSenderAccountId().toString());
        }
        System.out.println("=".repeat(120));
    }

    public void approveTransfer() {
        showPendingTransfers();

        List<Transaction> pending = transactionService.getPendingTransactions();
        if (pending.isEmpty()) {
            return;
        }

        System.out.print("\nEnter transaction ID to approve: ");
        String txIdStr = sc.nextLine().trim();

        UUID txId;
        try {
            txId = UUID.fromString(txIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Invalid transaction ID format.");
            return;
        }

        boolean success = accountService.completeTransfer(txId);
        if (success) {
            System.out.println("✓ Transfer approved and completed successfully!");
        } else {
            System.out.println("✗ Transfer approval failed (transaction not found, insufficient funds, or already processed).");
        }
    }
}