package services;

import entities.FeeRule;
import enums.TransactionType;
import repositories.FeeRuleRepository;
import repositories.FeeRuleRepositoryImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FeeRuleService {

    private final FeeRuleRepository repo = new FeeRuleRepositoryImpl();

    public boolean addRule(FeeRule rule) {
        return repo.insert(rule);
    }

    public boolean updateRule(FeeRule rule) {
        return repo.update(rule);
    }

    public boolean toggle(UUID id, boolean active) {
        return repo.toggleStatus(id, active);
    }

    public List<FeeRule> getAll() {
        return repo.findAll();
    }

    public Optional<FeeRule> getById(UUID id) {
        return repo.findById(id);
    }

    public BigDecimal computeFee(TransactionType type, BigDecimal amount) {
        List<FeeRule> rules = repo.findActiveByType(type);
        if (rules.isEmpty()) return BigDecimal.ZERO;

        FeeRule rule = rules.get(0);
        BigDecimal fee;
        if (rule.getModal() == enums.Modal.FIX) {
            fee = rule.getRuleValue();
        } else {
            fee = amount.multiply(rule.getRuleValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }
        return fee;
    }
}