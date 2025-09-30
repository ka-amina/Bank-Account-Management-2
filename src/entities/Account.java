package entities;

import enums.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private UUID accountId;
    private String accountNumber;
    private BigDecimal balance;
    private AccountType type;
    private boolean isActive = true;
    private UUID clientId;
    private int createdBy;

    public Account(String accountNumber, AccountType type, UUID clientId, int createdBy) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.clientId = clientId;
        this.createdBy = createdBy;
        this.balance = BigDecimal.ZERO;
    }

    public Account(){

    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
}
