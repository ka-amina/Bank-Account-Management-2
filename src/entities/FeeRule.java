package entities;

import enums.Modal;
import enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public class FeeRule {
    private UUID id;
    private Modal modal;
    private BigDecimal ruleValue;
    private TransactionType operationType;
    private boolean active;

    public FeeRule() {
    }

    public FeeRule(Modal modal, BigDecimal ruleValue,
                   TransactionType operationType, boolean active) {
        this.modal = modal;
        this.ruleValue = ruleValue;
        this.operationType = operationType;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Modal getModal() {
        return modal;
    }

    public void setModal(Modal modal) {
        this.modal = modal;
    }

    public BigDecimal getRuleValue() {
        return ruleValue;
    }

    public void setRuleValue(BigDecimal ruleValue) {
        this.ruleValue = ruleValue;
    }

    public TransactionType getOperationType() {
        return operationType;
    }

    public void setOperationType(TransactionType operationType) {
        this.operationType = operationType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("%s | %-15s | %6s | %8.4f | %s",
                id, operationType, modal, ruleValue, active ? "ON" : "OFF");
    }
}