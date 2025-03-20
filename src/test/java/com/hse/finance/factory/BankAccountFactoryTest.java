package com.hse.finance.factory;

import com.hse.finance.model.BankAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountFactoryTest {
    
    private BankAccountFactory factory;
    
    @BeforeEach
    void setUp() {
        factory = new BankAccountFactory();
    }
    
    @Test
    void createBankAccount_WithValidParameters_ShouldCreateAccount() {
        // Given
        String name = "Test Account";
        BigDecimal initialBalance = new BigDecimal("100.00");
        
        // When
        BankAccount account = factory.createBankAccount(name, initialBalance);
        
        // Then
        assertNotNull(account);
        assertNotNull(account.getId());
        assertEquals(name, account.getName());
        assertEquals(initialBalance, account.getBalance());
    }
    
    @Test
    void createBankAccount_WithNullName_ShouldThrowException() {
        // Given
        String name = null;
        BigDecimal initialBalance = BigDecimal.ZERO;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createBankAccount(name, initialBalance);
        });
    }
    
    @Test
    void createBankAccount_WithEmptyName_ShouldThrowException() {
        // Given
        String name = "";
        BigDecimal initialBalance = BigDecimal.ZERO;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createBankAccount(name, initialBalance);
        });
    }
    
    @Test
    void createBankAccount_WithNegativeBalance_ShouldThrowException() {
        // Given
        String name = "Test Account";
        BigDecimal initialBalance = new BigDecimal("-10.00");
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createBankAccount(name, initialBalance);
        });
    }
    
    @Test
    void createBankAccount_WithNullBalance_ShouldSetZeroBalance() {
        // Given
        String name = "Test Account";
        BigDecimal initialBalance = null;
        
        // When
        BankAccount account = factory.createBankAccount(name, initialBalance);
        
        // Then
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }
} 