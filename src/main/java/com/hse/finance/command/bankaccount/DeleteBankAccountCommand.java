package com.hse.finance.command.bankaccount;

import com.hse.finance.command.Command;
import com.hse.finance.proxy.BankAccountRepositoryProxy;
import com.hse.finance.service.repository.OperationRepository;

import java.util.UUID;

/**
 * Command to delete a bank account and all its operations
 */
public class DeleteBankAccountCommand implements Command<Boolean> {
    private BankAccountRepositoryProxy bankAccountRepository;
    private OperationRepository operationRepository;
    private UUID accountId;
    
    /**
     * Default constructor
     */
    public DeleteBankAccountCommand() {
    }
    
    /**
     * Constructor with all required parameters
     */
    public DeleteBankAccountCommand(BankAccountRepositoryProxy bankAccountRepository, 
                                  OperationRepository operationRepository, UUID accountId) {
        this.bankAccountRepository = bankAccountRepository;
        this.operationRepository = operationRepository;
        this.accountId = accountId;
    }

    @Override
    public Boolean execute() {
        if (!bankAccountRepository.findById(accountId).isPresent()) {
            return false;
        }
        
        // First, delete all operations associated with this account
        operationRepository.deleteByBankAccountId(accountId);
        
        // Then delete the account itself
        return bankAccountRepository.deleteById(accountId);
    }
} 