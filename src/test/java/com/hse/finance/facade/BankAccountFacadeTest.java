package com.hse.finance.facade;

import com.hse.finance.factory.BankAccountFactory;
import com.hse.finance.model.BankAccount;
import com.hse.finance.proxy.BankAccountRepositoryProxy;
import com.hse.finance.service.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountFacadeTest {

    // Вместо мока напрямую создаем экземпляр
    private BankAccountFactory bankAccountFactory;
    
    @Mock
    private BankAccountRepositoryProxy bankAccountRepository;
    
    @Mock
    private OperationRepository operationRepository;
    
    private BankAccountFacade facade;
    
    @BeforeEach
    void setUp() {
        bankAccountFactory = new BankAccountFactory();
        facade = new BankAccountFacade(bankAccountFactory, bankAccountRepository, operationRepository);
    }
    
    @Test
    void createBankAccount_ShouldCreateAndReturnAccount() {
        // Given
        String name = "Test Account";
        BigDecimal balance = new BigDecimal("100.00");
        
        // When
        BankAccount result = facade.createBankAccount(name, balance);
        
        // Then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(name, result.getName());
        assertEquals(balance, result.getBalance());
        verify(bankAccountRepository).save(any(BankAccount.class));
    }
    
    @Test
    void updateBankAccount_WithValidId_ShouldUpdateAndReturnAccount() {
        // Given
        UUID accountId = UUID.randomUUID();
        String newName = "Updated Account";
        BankAccount existingAccount = BankAccount.builder()
                .id(accountId)
                .name("Original Name")
                .balance(BigDecimal.ZERO)
                .build();
        
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        
        // When
        Optional<BankAccount> result = facade.updateBankAccount(accountId, newName);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(newName, result.get().getName());
        verify(bankAccountRepository).findById(accountId);
        verify(bankAccountRepository).save(any(BankAccount.class));
    }
    
    @Test
    void updateBankAccount_WithInvalidId_ShouldReturnEmpty() {
        // Given
        UUID accountId = UUID.randomUUID();
        String newName = "Updated Account";
        
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.empty());
        
        // When
        Optional<BankAccount> result = facade.updateBankAccount(accountId, newName);
        
        // Then
        assertFalse(result.isPresent());
        verify(bankAccountRepository).findById(accountId);
        verify(bankAccountRepository, never()).save(any(BankAccount.class));
    }
    
    @Test
    void deleteBankAccount_WithValidId_ShouldDeleteAndReturnTrue() {
        // Given
        UUID accountId = UUID.randomUUID();
        
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(mock(BankAccount.class)));
        when(bankAccountRepository.deleteById(accountId)).thenReturn(true);
        
        // When
        boolean result = facade.deleteBankAccount(accountId);
        
        // Then
        assertTrue(result);
        verify(operationRepository).deleteByBankAccountId(accountId);
        verify(bankAccountRepository).deleteById(accountId);
    }
    
    @Test
    void deleteBankAccount_WithInvalidId_ShouldReturnFalse() {
        // Given
        UUID accountId = UUID.randomUUID();
        
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.empty());
        
        // When
        boolean result = facade.deleteBankAccount(accountId);
        
        // Then
        assertFalse(result);
        verify(operationRepository, never()).deleteByBankAccountId(any(UUID.class));
        verify(bankAccountRepository, never()).deleteById(any(UUID.class));
    }
    
    @Test
    void getAllBankAccounts_ShouldReturnAllAccounts() {
        // Given
        List<BankAccount> expectedAccounts = new ArrayList<>();
        expectedAccounts.add(mock(BankAccount.class));
        expectedAccounts.add(mock(BankAccount.class));
        
        when(bankAccountRepository.findAll()).thenReturn(expectedAccounts);
        
        // When
        List<BankAccount> result = facade.getAllBankAccounts();
        
        // Then
        assertEquals(expectedAccounts, result);
        verify(bankAccountRepository).findAll();
    }
    
    @Test
    void getBankAccountById_WithValidId_ShouldReturnAccount() {
        // Given
        UUID accountId = UUID.randomUUID();
        BankAccount expectedAccount = mock(BankAccount.class);
        
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.of(expectedAccount));
        
        // When
        Optional<BankAccount> result = facade.getBankAccountById(accountId);
        
        // Then
        assertTrue(result.isPresent());
        assertEquals(expectedAccount, result.get());
        verify(bankAccountRepository).findById(accountId);
    }
    
    @Test
    void getBankAccountById_WithInvalidId_ShouldReturnEmpty() {
        // Given
        UUID accountId = UUID.randomUUID();
        
        when(bankAccountRepository.findById(accountId)).thenReturn(Optional.empty());
        
        // When
        Optional<BankAccount> result = facade.getBankAccountById(accountId);
        
        // Then
        assertFalse(result.isPresent());
        verify(bankAccountRepository).findById(accountId);
    }
} 