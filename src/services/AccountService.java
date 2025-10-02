package services;

import entities.Account;
import entities.Transaction;
import enums.AccountType;
import enums.TransactionType;
import repositories.AccountRepositoryImpl;
import repositories.AccoutRepository;

import java.math.BigDecimal;
import java.util.List;
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
        boolean desposit = accoutRepository.deposit(accountNumber, amount);
        if (!desposit) return false;

        UUID accountId = accoutRepository.findIdByNumber(accountNumber).orElse(null);
        Transaction transaction = new Transaction(amount, TransactionType.DEPOSIT,
                null,
                accountId);
        return transactionService.add(transaction);
    }

    public boolean withdraw(String accountNumber, BigDecimal amount) {
        // 1. update balance
        boolean withdraw = accoutRepository.withdraw(accountNumber, amount);
        if (!withdraw) return false;

        // 2. record transaction
        UUID accountId = accoutRepository.findIdByNumber(accountNumber).orElse(null);
        Transaction tx = new Transaction(amount, TransactionType.WITHDRAW, accountId, null);
        return withdraw;
    }
}
