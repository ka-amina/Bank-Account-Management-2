package repositories;

import entities.FeeRule;
import enums.TransactionType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FeeRuleRepository {
    boolean insert(FeeRule rule);

    boolean update(FeeRule rule);

    boolean toggleStatus(UUID id, boolean active);

    List<FeeRule> findAll();

    List<FeeRule> findActiveByType(TransactionType type);

    Optional<FeeRule> findById(UUID id);
}