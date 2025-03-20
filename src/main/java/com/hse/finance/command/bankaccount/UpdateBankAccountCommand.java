package com.hse.finance.command.bankaccount;

import com.hse.finance.command.Command;
import com.hse.finance.model.BankAccount;
import com.hse.finance.proxy.BankAccountRepositoryProxy;

import java.util.UUID;

/**
 * Command to update an existing bank account
 */
public class UpdateBankAccountCommand implements Command<BankAccount> {
    private BankAccountRepositoryProxy repository;
    private UUID accountId;
    private String newName;
    
    /**
     * Default constructor
     */
    public UpdateBankAccountCommand() {
    }
    
    /**
     * Constructor with all required parameters
     */
    public UpdateBankAccountCommand(BankAccountRepositoryProxy repository, UUID accountId, String newName) {
        this.repository = repository;
        this.accountId = accountId;
        this.newName = newName;
    }

    @Override
    public BankAccount execute() {
        BankAccount account = repository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found with ID: " + accountId));
        
        account.setName(newName);
        repository.save(account);
        
        return account;
    }
} 