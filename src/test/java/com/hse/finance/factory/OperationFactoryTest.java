package com.hse.finance.factory;

import com.hse.finance.model.Operation;
import com.hse.finance.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OperationFactoryTest {
    
    private OperationFactory factory;
    
    @BeforeEach
    void setUp() {
        factory = new OperationFactory();
    }
    
    @Test
    void createOperation_WithValidParameters_ShouldCreateOperation() {
        // Given
        OperationType type = OperationType.INCOME;
        UUID bankAccountId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");
        UUID categoryId = UUID.randomUUID();
        String description = "Test description";
        
        // When
        Operation operation = factory.createOperation(type, bankAccountId, amount, categoryId, description);
        
        // Then
        assertNotNull(operation);
        assertNotNull(operation.getId());
        assertEquals(type, operation.getType());
        assertEquals(bankAccountId, operation.getBankAccountId());
        assertEquals(amount, operation.getAmount());
        assertEquals(categoryId, operation.getCategoryId());
        assertEquals(description, operation.getDescription());
        assertNotNull(operation.getDate());
    }
    
    @Test
    void createOperation_WithNullType_ShouldThrowException() {
        // Given
        OperationType type = null;
        UUID bankAccountId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");
        UUID categoryId = UUID.randomUUID();
        String description = "Test description";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createOperation(type, bankAccountId, amount, categoryId, description);
        });
    }
    
    @Test
    void createOperation_WithNullBankAccountId_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        UUID bankAccountId = null;
        BigDecimal amount = new BigDecimal("100.00");
        UUID categoryId = UUID.randomUUID();
        String description = "Test description";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createOperation(type, bankAccountId, amount, categoryId, description);
        });
    }
    
    @Test
    void createOperation_WithNullAmount_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        UUID bankAccountId = UUID.randomUUID();
        BigDecimal amount = null;
        UUID categoryId = UUID.randomUUID();
        String description = "Test description";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createOperation(type, bankAccountId, amount, categoryId, description);
        });
    }
    
    @Test
    void createOperation_WithZeroAmount_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        UUID bankAccountId = UUID.randomUUID();
        BigDecimal amount = BigDecimal.ZERO;
        UUID categoryId = UUID.randomUUID();
        String description = "Test description";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createOperation(type, bankAccountId, amount, categoryId, description);
        });
    }
    
    @Test
    void createOperation_WithNegativeAmount_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        UUID bankAccountId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("-10.00");
        UUID categoryId = UUID.randomUUID();
        String description = "Test description";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createOperation(type, bankAccountId, amount, categoryId, description);
        });
    }
    
    @Test
    void createOperation_WithNullCategoryId_ShouldThrowException() {
        // Given
        OperationType type = OperationType.INCOME;
        UUID bankAccountId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");
        UUID categoryId = null;
        String description = "Test description";
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createOperation(type, bankAccountId, amount, categoryId, description);
        });
    }
    
    @Test
    void createOperation_WithNullDescription_ShouldCreateOperation() {
        // Given
        OperationType type = OperationType.INCOME;
        UUID bankAccountId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("100.00");
        UUID categoryId = UUID.randomUUID();
        String description = null;
        
        // When
        Operation operation = factory.createOperation(type, bankAccountId, amount, categoryId, description);
        
        // Then
        assertNotNull(operation);
        assertEquals(description, operation.getDescription());
    }
} 