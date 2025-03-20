package com.hse.finance.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a financial operation entity
 */
public class Operation {
    private UUID id;
    private OperationType type;
    private UUID bankAccountId;
    private BigDecimal amount;
    private LocalDateTime date;
    private UUID categoryId;
    private String description;

    // Default constructor
    public Operation() {
    }

    // All args constructor
    public Operation(UUID id, OperationType type, UUID bankAccountId, BigDecimal amount,
                    LocalDateTime date, UUID categoryId, String description) {
        this.id = id;
        this.type = type;
        this.bankAccountId = bankAccountId;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.description = description;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public UUID getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(UUID bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Builder pattern implementation
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private OperationType type;
        private UUID bankAccountId;
        private BigDecimal amount;
        private LocalDateTime date;
        private UUID categoryId;
        private String description;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder type(OperationType type) {
            this.type = type;
            return this;
        }

        public Builder bankAccountId(UUID bankAccountId) {
            this.bankAccountId = bankAccountId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder categoryId(UUID categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Operation build() {
            return new Operation(id, type, bankAccountId, amount, date, categoryId, description);
        }
    }
} 