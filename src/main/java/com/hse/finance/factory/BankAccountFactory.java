package com.hse.finance.factory;

import com.hse.finance.model.BankAccount;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Factory for creating BankAccount objects
 */
@Component
public class BankAccountFactory {

    /**
     * Creates a new bank account with validated parameters
     */
    public BankAccount createBankAccount(String name, BigDecimal initialBalance) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account name cannot be empty");
        }
        
        if (initialBalance == null) {
            initialBalance = BigDecimal.ZERO;
        } else if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        
        return BankAccount.builder()
                .id(UUID.randomUUID())
                .name(name)
                .balance(initialBalance)
                .build();
    }
} 