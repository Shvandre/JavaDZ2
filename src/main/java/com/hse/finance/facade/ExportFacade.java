package com.hse.finance.facade;

import com.hse.finance.model.BankAccount;
import com.hse.finance.model.Category;
import com.hse.finance.model.Operation;
import com.hse.finance.proxy.BankAccountRepositoryProxy;
import com.hse.finance.service.repository.CategoryRepository;
import com.hse.finance.service.repository.OperationRepository;
import com.hse.finance.visitor.ExportVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Facade for exporting finance data
 */
@Service
public class ExportFacade {
    private BankAccountRepositoryProxy bankAccountRepository;
    private CategoryRepository categoryRepository;
    private OperationRepository operationRepository;

    @Autowired
    public ExportFacade(BankAccountRepositoryProxy bankAccountRepository, CategoryRepository categoryRepository,
                       OperationRepository operationRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    /**
     * Export all finance data using the provided visitor
     */
    public void exportData(ExportVisitor visitor, String outputFilePath) throws IOException {
        // Get all data from repositories
        List<BankAccount> accounts = bankAccountRepository.findAll();
        List<Category> categories = categoryRepository.findAll();
        List<Operation> operations = operationRepository.findAll();
        
        // Visit all data objects
        visitor.visitBankAccounts(accounts);
        visitor.visitCategories(categories);
        visitor.visitOperations(operations);
        
        // Get result and write to file
        String result = visitor.getResult();
        writeToFile(result, outputFilePath);
    }
    
    private void writeToFile(String content, String filePath) throws IOException {
        File file = new File(filePath);
        
        // Create parent directories if they don't exist
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }
        
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }
} 