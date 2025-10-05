package repositories;

import entities.Credit;
import enums.CreditStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditRepository {
    boolean createCredit(Credit credit);

    Optional<Credit> findById(UUID creditId);

    List<Credit> findByAccountId(String accountNumber);

    List<Credit> findAll();

    List<Credit> findByStatus(CreditStatus status);

    boolean updateStatus(UUID creditId, CreditStatus status);
}