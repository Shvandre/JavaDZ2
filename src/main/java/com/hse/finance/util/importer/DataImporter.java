package com.hse.finance.util.importer;

import com.hse.finance.facade.BankAccountFacade;
import com.hse.finance.facade.CategoryFacade;
import com.hse.finance.facade.OperationFacade;
import com.hse.finance.model.BankAccount;
import com.hse.finance.model.Category;
import com.hse.finance.model.OperationType;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Abstract class for importing data from different file formats
 * Uses Template Method pattern
 */
public abstract class DataImporter {
    protected BankAccountFacade bankAccountFacade;
    protected CategoryFacade categoryFacade;
    protected OperationFacade operationFacade;
    
    /**
     * Default constructor
     */
    public DataImporter() {
    }
    
    /**
     * Constructor with all required parameters
     */
    public DataImporter(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade, 
                       OperationFacade operationFacade) {
        this.bankAccountFacade = bankAccountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
    }
    
    /**
     * Template method defining the algorithm for data import
     */
    public final void importData(File file) throws IOException {
        // Check if file exists and is readable
        if (!file.exists() || !file.canRead()) {
            throw new IOException("Cannot read file: " + file.getAbsolutePath());
        }
        
        // Parse the file and get data maps
        Map<String, Object> data = parseFile(file);
        
        // Process bank accounts
        processAccounts(data);
        
        // Process categories
        processCategories(data);
        
        // Process operations
        processOperations(data);
    }
    
    /**
     * Parse the import file and return a map of data
     * This method will be implemented by subclasses for different file formats
     */
    protected abstract Map<String, Object> parseFile(File file) throws IOException;
    
    /**
     * Process bank account data
     */
    private void processAccounts(Map<String, Object> data) {
        if (data.containsKey("accounts")) {
            List<Map<String, Object>> accounts = (List<Map<String, Object>>) data.get("accounts");
            
            for (Map<String, Object> accountData : accounts) {
                String name = (String) accountData.get("name");
                BigDecimal balance = new BigDecimal(accountData.get("balance").toString());
                
                bankAccountFacade.createBankAccount(name, balance);
            }
        }
    }
    
    /**
     * Process category data
     */
    private void processCategories(Map<String, Object> data) {
        if (data.containsKey("categories")) {
            List<Map<String, Object>> categories = (List<Map<String, Object>>) data.get("categories");
            
            for (Map<String, Object> categoryData : categories) {
                String name = (String) categoryData.get("name");
                OperationType type = OperationType.valueOf((String) categoryData.get("type"));
                
                categoryFacade.createCategory(name, type);
            }
        }
    }
    
    /**
     * Process operation data
     */
    private void processOperations(Map<String, Object> data) {
        if (data.containsKey("operations")) {
            List<Map<String, Object>> operations = (List<Map<String, Object>>) data.get("operations");
            
            // Get all accounts and categories for lookup
            List<BankAccount> accounts = bankAccountFacade.getAllBankAccounts();
            List<Category> categories = categoryFacade.getAllCategories();
            
            for (Map<String, Object> operationData : operations) {
                OperationType type = OperationType.valueOf((String) operationData.get("type"));
                BigDecimal amount = new BigDecimal(operationData.get("amount").toString());
                String description = (String) operationData.get("description");
                
                // Find account by name
                String accountName = (String) operationData.get("account_name");
                UUID accountId = accounts.stream()
                        .filter(a -> a.getName().equals(accountName))
                        .findFirst()
                        .map(BankAccount::getId)
                        .orElse(null);
                
                if (accountId == null) {
                    continue; // Skip if account not found
                }
                
                // Find category by name and type
                String categoryName = (String) operationData.get("category_name");
                UUID categoryId = categories.stream()
                        .filter(c -> c.getName().equals(categoryName) && c.getType() == type)
                        .findFirst()
                        .map(Category::getId)
                        .orElse(null);
                
                if (categoryId == null) {
                    continue; // Skip if category not found
                }
                
                // Create operation
                operationFacade.createOperation(type, accountId, amount, categoryId, description);
            }
        }
    }
} 