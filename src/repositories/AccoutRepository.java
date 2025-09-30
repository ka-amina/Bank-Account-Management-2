package repositories;

import entities.Account;

import java.util.List;

public interface AccoutRepository {
    boolean createAccount(Account account);

    List<Account> findByCreatedBy(int userId);

    boolean deactivate(String accountNumber, int createdBy);
}
