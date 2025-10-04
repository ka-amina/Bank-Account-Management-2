package entities;

import enums.TransactionStatus;
import enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private LocalDateTime timestamp;
    private UUID senderAccountId;
    private UUID receiverAccountId;

    public Transaction(BigDecimal amount, TransactionType type,
                       UUID senderAccountId, UUID receiverAccountId) {
        this.amount = amount;
        this.type = type;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.timestamp = LocalDateTime.now();
        this.status = TransactionStatus.COMPLETED;
    }

    public Transaction(BigDecimal amount, TransactionType type,
                       UUID senderAccountId, UUID receiverAccountId,
                       TransactionStatus status) {
        this.amount = amount;
        this.type = type;
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.timestamp = LocalDateTime.now();
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public UUID getSenderAccountId() {
        return senderAccountId;
    }

    public UUID getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setReceiverAccountId(UUID receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public void setSenderAccountId(UUID senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}