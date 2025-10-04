package services;

import entities.Account;
import entities.Transaction;
import enums.AccountType;
import enums.TransactionStatus;
import enums.TransactionType;
import repositories.AccountRepositoryImpl;
import repositories.AccoutRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AccountService {
    private final AccoutRepository accoutRepository = new AccountRepositoryImpl();
    private final TransactionService transactionService = new TransactionService();

    public Account createAccount(String accountNumber, AccountType type, UUID clientId, int createdBy) {
        Account account = new Account(accountNumber, type, clientId, createdBy);
        if (accoutRepository.createAccount(account)) {
            return account;
        }
        return null;
    }

    public List<Account> getAccounts() {
        return accoutRepository.findAll();
    }

    public boolean closeAccount(String accountNumber) {
        return accoutRepository.deactivate(accountNumber);
    }

    public boolean deposit(String accountNumber, BigDecimal amount) {
        boolean deposit = accoutRepository.deposit(accountNumber, amount);
        if (!deposit) return false;

        UUID accountId = accoutRepository.findIdByNumber(accountNumber).orElse(null);
        Transaction transaction = new Transaction(amount, TransactionType.DEPOSIT,
                null, accountId);
        return transactionService.add(transaction);
    }

    public boolean withdraw(String accountNumber, BigDecimal amount) {
        boolean withdraw = accoutRepository.withdraw(accountNumber, amount);
        if (!withdraw) return false;

        UUID accountId = accoutRepository.findIdByNumber(accountNumber).orElse(null);
        Transaction tx = new Transaction(amount, TransactionType.WITHDRAW, accountId, null);
        return transactionService.add(tx);
    }

    public Optional<Account> findByNumber(String accountNumber) {
        return accoutRepository.findByNumber(accountNumber);
    }

    public Optional<Account> findById(UUID accountId) {
        return accoutRepository.findById(accountId);
    }

    public List<Account> getAccountsByClientId(UUID clientId) {
        return accoutRepository.findByClientId(clientId);
    }


    public boolean transferInternal(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        Optional<Account> fromAcc = findByNumber(fromAccountNumber);
        Optional<Account> toAcc = findByNumber(toAccountNumber);

        if (!fromAcc.isPresent() || !toAcc.isPresent()) {
            return false;
        }

        if (!fromAcc.get().isActive() || !toAcc.get().isActive()) {
            return false;
        }

        boolean withdrawn = accoutRepository.withdraw(fromAccountNumber, amount);
        if (!withdrawn) return false;

        boolean deposited = accoutRepository.deposit(toAccountNumber, amount);
        if (!deposited) {
            accoutRepository.deposit(fromAccountNumber, amount);
            return false;
        }

        Transaction txOut = new Transaction(amount, TransactionType.TRANSFER_OUT,
                fromAcc.get().getAccountId(), toAcc.get().getAccountId());
        transactionService.add(txOut);

        Transaction txIn = new Transaction(amount, TransactionType.TRANSFER_IN,
                fromAcc.get().getAccountId(), toAcc.get().getAccountId());
        transactionService.add(txIn);

        return true;
    }

    public boolean transferExternal(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        Optional<Account> fromAcc = findByNumber(fromAccountNumber);
        Optional<Account> toAcc = findByNumber(toAccountNumber);

        if (!fromAcc.isPresent() || !toAcc.isPresent()) {
            return false;
        }

        if (!fromAcc.get().isActive() || !toAcc.get().isActive()) {
            return false;
        }

        if (fromAcc.get().getBalance().compareTo(amount) < 0) {
            return false;
        }

        Transaction txPending = new Transaction(amount, TransactionType.TRANSFER_EXTERNAL,
                fromAcc.get().getAccountId(), toAcc.get().getAccountId(),
                TransactionStatus.PENDING);
        return transactionService.add(txPending);
    }

    public boolean completeTransfer(UUID transactionId) {
        Optional<Transaction> txOpt = transactionService.findById(transactionId);
        if (!txOpt.isPresent()) return false;

        Transaction tx = txOpt.get();
        if (tx.getStatus() != TransactionStatus.PENDING) return false;

        Optional<Account> fromAcc = findById(tx.getSenderAccountId());
        Optional<Account> toAcc = findById(tx.getReceiverAccountId());

        if (!fromAcc.isPresent() || !toAcc.isPresent()) {
            transactionService.updateStatus(transactionId, TransactionStatus.FAILED);
            return false;
        }

        boolean withdrawn = accoutRepository.withdraw(fromAcc.get().getAccountNumber(), tx.getAmount());
        if (!withdrawn) {
            transactionService.updateStatus(transactionId, TransactionStatus.FAILED);
            return false;
        }

        boolean deposited = accoutRepository.deposit(toAcc.get().getAccountNumber(), tx.getAmount());
        if (!deposited) {
            accoutRepository.deposit(fromAcc.get().getAccountNumber(), tx.getAmount());
            transactionService.updateStatus(transactionId, TransactionStatus.FAILED);
            return false;
        }

        return transactionService.updateStatus(transactionId, TransactionStatus.COMPLETED);
    }
}