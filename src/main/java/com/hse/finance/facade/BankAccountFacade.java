package com.hse.finance.facade;

import com.hse.finance.command.Command;
import com.hse.finance.command.TimedCommandDecorator;
import com.hse.finance.command.bankaccount.CreateBankAccountCommand;
import com.hse.finance.command.bankaccount.DeleteBankAccountCommand;
import com.hse.finance.command.bankaccount.UpdateBankAccountCommand;
import com.hse.finance.factory.BankAccountFactory;
import com.hse.finance.model.BankAccount;
import com.hse.finance.proxy.BankAccountRepositoryProxy;
import com.hse.finance.service.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Facade for bank account operations
 */
@Service
public class BankAccountFacade {
    private BankAccountFactory bankAccountFactory;
    private BankAccountRepositoryProxy bankAccountRepository;
    private OperationRepository operationRepository;

    @Autowired
    public BankAccountFacade(BankAccountFactory bankAccountFactory,
                           BankAccountRepositoryProxy bankAccountRepository,
                           OperationRepository operationRepository) {
        this.bankAccountFactory = bankAccountFactory;
        this.bankAccountRepository = bankAccountRepository;
        this.operationRepository = operationRepository;
    }

    public BankAccount createBankAccount(String name, BigDecimal initialBalance) {
        Command<BankAccount> command = new CreateBankAccountCommand(
                bankAccountFactory, bankAccountRepository, name, initialBalance);
        
        Command<BankAccount> timedCommand = new TimedCommandDecorator<>(command, "CreateBankAccount");
        
        return timedCommand.execute();
    }

    public Optional<BankAccount> updateBankAccount(UUID accountId, String newName) {
        if (accountId == null || newName == null || newName.trim().isEmpty()) {
            return Optional.empty();
        }

        Command<BankAccount> command = new UpdateBankAccountCommand(
                bankAccountRepository, accountId, newName);
        
        Command<BankAccount> timedCommand = new TimedCommandDecorator<>(command, "UpdateBankAccount");
        
        try {
            return Optional.of(timedCommand.execute());
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public boolean deleteBankAccount(UUID accountId) {
        Command<Boolean> command = new DeleteBankAccountCommand(
                bankAccountRepository, operationRepository, accountId);
        
        Command<Boolean> timedCommand = new TimedCommandDecorator<>(command, "DeleteBankAccount");
        
        return timedCommand.execute();
    }

    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    public Optional<BankAccount> getBankAccountById(UUID id) {
        return bankAccountRepository.findById(id);
    }

    public void recalculateBalance(UUID accountId) {
        BankAccount account = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Bank account not found with ID: " + accountId));
        
        BigDecimal calculatedBalance = BigDecimal.ZERO;
        // Get all operations for this account and recalculate the balance
        // For INCOME operations, add to the balance
        // For EXPENSE operations, subtract from the balance
        
        account.setBalance(calculatedBalance);
        bankAccountRepository.save(account);
    }
} 