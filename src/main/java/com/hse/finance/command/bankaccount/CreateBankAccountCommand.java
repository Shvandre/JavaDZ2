package com.hse.finance.command.bankaccount;

import com.hse.finance.command.Command;
import com.hse.finance.factory.BankAccountFactory;
import com.hse.finance.model.BankAccount;
import com.hse.finance.proxy.BankAccountRepositoryProxy;

import java.math.BigDecimal;

/**
 * Command to create a new bank account
 */
public class CreateBankAccountCommand implements Command<BankAccount> {
    private BankAccountFactory factory;
    private BankAccountRepositoryProxy repository;
    private String name;
    private BigDecimal initialBalance;
    
    /**
     * Default constructor
     */
    public CreateBankAccountCommand() {
    }
    
    /**
     * Constructor with all required parameters
     */
    public CreateBankAccountCommand(BankAccountFactory factory, BankAccountRepositoryProxy repository, 
                                 String name, BigDecimal initialBalance) {
        this.factory = factory;
        this.repository = repository;
        this.name = name;
        this.initialBalance = initialBalance;
    }

    @Override
    public BankAccount execute() {
        BankAccount account = factory.createBankAccount(name, initialBalance);
        repository.save(account);
        return account;
    }
} 