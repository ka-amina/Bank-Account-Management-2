package repositories;

import entities.Account;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccoutRepository {
    boolean createAccount(Account account);
    List<Account> findAll();
    boolean deactivate(String accountNumber);
    Optional<UUID> findIdByNumber(String accountNumber);
    boolean deposit(String accountNumber, BigDecimal amount);
    boolean withdraw(String accountNumber, BigDecimal amount);
    Optional<Account> findByNumber(String accountNumber);
    Optional<Account> findById(UUID accountId);
    List<Account> findByClientId(UUID clientId);
}