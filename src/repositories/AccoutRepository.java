package repositories;

import entities.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccoutRepository {
    boolean createAccount(Account account);

    List<Account> findByCreatedBy(int userId);

    boolean deactivate(String accountNumber, int createdBy);

    boolean deposit(String accountNumber, BigDecimal amount, int createdBy);

    Optional<UUID> findIdByNumber(String accountNumber);
}
