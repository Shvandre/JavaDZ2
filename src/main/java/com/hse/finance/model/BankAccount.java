package com.hse.finance.model;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Represents a banking account entity
 */
public class BankAccount {
    private UUID id;
    private String name;
    private BigDecimal balance;

    // Default constructor
    public BankAccount() {
    }

    // All args constructor
    public BankAccount(UUID id, String name, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    // Builder pattern implementation
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private BigDecimal balance;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public BankAccount build() {
            return new BankAccount(id, name, balance);
        }
    }
} 