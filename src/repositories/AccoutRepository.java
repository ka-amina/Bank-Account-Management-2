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

    boolean deposit(String accountNumber, BigDecimal amount);

    Optional<UUID> findIdByNumber(String accountNumber);

    boolean withdraw(String accountNumber, BigDecimal amount);
}
