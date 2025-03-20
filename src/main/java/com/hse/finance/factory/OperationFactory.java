package com.hse.finance.factory;

import com.hse.finance.model.Operation;
import com.hse.finance.model.OperationType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Factory for creating Operation objects
 */
@Component
public class OperationFactory {

    /**
     * Creates a new operation with validated parameters
     */
    public Operation createOperation(OperationType type, UUID bankAccountId, 
                                      BigDecimal amount, UUID categoryId, 
                                      String description) {
        if (type == null) {
            throw new IllegalArgumentException("Operation type cannot be null");
        }
        
        if (bankAccountId == null) {
            throw new IllegalArgumentException("Bank account ID cannot be null");
        }
        
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }
        
        return Operation.builder()
                .id(UUID.randomUUID())
                .type(type)
                .bankAccountId(bankAccountId)
                .amount(amount)
                .date(LocalDateTime.now())
                .categoryId(categoryId)
                .description(description)
                .build();
    }
} 