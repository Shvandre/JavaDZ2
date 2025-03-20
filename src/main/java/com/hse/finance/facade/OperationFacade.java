package com.hse.finance.facade;

import com.hse.finance.factory.OperationFactory;
import com.hse.finance.model.BankAccount;
import com.hse.finance.model.Category;
import com.hse.finance.model.Operation;
import com.hse.finance.model.OperationType;
import com.hse.finance.proxy.BankAccountRepositoryProxy;
import com.hse.finance.service.repository.CategoryRepository;
import com.hse.finance.service.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Facade for operation-related functionality
 */
@Service
public class OperationFacade {
    private OperationFactory operationFactory;
    private OperationRepository operationRepository;
    private BankAccountRepositoryProxy bankAccountRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public OperationFacade(OperationFactory operationFactory, OperationRepository operationRepository,
                          BankAccountRepositoryProxy bankAccountRepository, CategoryRepository categoryRepository) {
        this.operationFactory = operationFactory;
        this.operationRepository = operationRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
    }

    public Operation createOperation(OperationType type, UUID bankAccountId, 
                                      BigDecimal amount, UUID categoryId, 
                                      String description) {
        // Validate that the bank account and category exist
        BankAccount account = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found with ID: " + bankAccountId));
                
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
        
        // Validate that the category type matches the operation type
        if (category.getType() != type) {
            throw new IllegalArgumentException(
                    "Category type " + category.getType() + " does not match operation type " + type);
        }
        
        // Create the operation
        Operation operation = operationFactory.createOperation(type, bankAccountId, amount, categoryId, description);
        
        // Update the account balance
        if (type == OperationType.INCOME) {
            account.setBalance(account.getBalance().add(amount));
        } else {
            // Проверяем, не станет ли баланс отрицательным перед вычитанием
            BigDecimal newBalance = account.getBalance().subtract(amount);
            if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalStateException("Operation would cause account balance to become negative");
            }
            account.setBalance(newBalance);
        }
        
        // Save the operation and the updated account
        operationRepository.save(operation);
        bankAccountRepository.save(account);
        
        return operation;
    }

    public boolean deleteOperation(UUID operationId) {
        Optional<Operation> operationOpt = operationRepository.findById(operationId);
        if (!operationOpt.isPresent()) {
            return false;
        }
        
        Operation operation = operationOpt.get();
        BankAccount account = bankAccountRepository.findById(operation.getBankAccountId())
                .orElseThrow(() -> new IllegalStateException("Bank account not found for operation: " + operationId));
        
        // Reverse the effect on the bank account balance
        if (operation.getType() == OperationType.INCOME) {
            account.setBalance(account.getBalance().subtract(operation.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(operation.getAmount()));
        }
        
        // Delete the operation and update the account
        boolean deleted = operationRepository.deleteById(operationId);
        bankAccountRepository.save(account);
        
        return deleted;
    }

    public List<Operation> getAllOperations() {
        return operationRepository.findAll();
    }

    public List<Operation> getOperationsByAccountId(UUID accountId) {
        return operationRepository.findByBankAccountId(accountId);
    }

    public List<Operation> getOperationsByCategoryId(UUID categoryId) {
        return operationRepository.findByCategoryId(categoryId);
    }

    public List<Operation> getOperationsByType(OperationType type) {
        return operationRepository.findByType(type);
    }

    public List<Operation> getOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return operationRepository.findByDateBetween(startDate, endDate);
    }

    public Optional<Operation> getOperationById(UUID id) {
        return operationRepository.findById(id);
    }
} 