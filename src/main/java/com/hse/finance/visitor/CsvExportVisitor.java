package com.hse.finance.visitor;

import com.hse.finance.model.BankAccount;
import com.hse.finance.model.Category;
import com.hse.finance.model.Operation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;

/**
 * Visitor for exporting data to CSV format
 */
@Component
public class CsvExportVisitor implements ExportVisitor {
    private final StringBuilder accountsCsv;
    private final StringBuilder categoriesCsv;
    private final StringBuilder operationsCsv;

    public CsvExportVisitor() {
        this.accountsCsv = new StringBuilder();
        this.categoriesCsv = new StringBuilder();
        this.operationsCsv = new StringBuilder();
    }

    @Override
    public void visitBankAccounts(List<BankAccount> accounts) {
        accountsCsv.append("id,name,balance\n");
        
        for (BankAccount account : accounts) {
            StringJoiner joiner = new StringJoiner(",");
            joiner.add(account.getId().toString());
            joiner.add(escapeField(account.getName()));
            joiner.add(account.getBalance().toString());
            
            accountsCsv.append(joiner).append("\n");
        }
    }

    @Override
    public void visitCategories(List<Category> categories) {
        categoriesCsv.append("id,name,type\n");
        
        for (Category category : categories) {
            StringJoiner joiner = new StringJoiner(",");
            joiner.add(category.getId().toString());
            joiner.add(escapeField(category.getName()));
            joiner.add(category.getType().name());
            
            categoriesCsv.append(joiner).append("\n");
        }
    }

    @Override
    public void visitOperations(List<Operation> operations) {
        operationsCsv.append("id,type,amount,date,bank_account_id,category_id,description\n");
        
        for (Operation operation : operations) {
            StringJoiner joiner = new StringJoiner(",");
            joiner.add(operation.getId().toString());
            joiner.add(operation.getType().name());
            joiner.add(operation.getAmount().toString());
            joiner.add(operation.getDate().toString());
            joiner.add(operation.getBankAccountId().toString());
            joiner.add(operation.getCategoryId().toString());
            
            String description = operation.getDescription() != null ? 
                    escapeField(operation.getDescription()) : "";
            joiner.add(description);
            
            operationsCsv.append(joiner).append("\n");
        }
    }

    @Override
    public String getResult() {
        StringBuilder result = new StringBuilder();
        result.append("=== BANK ACCOUNTS ===\n")
              .append(accountsCsv)
              .append("\n=== CATEGORIES ===\n")
              .append(categoriesCsv)
              .append("\n=== OPERATIONS ===\n")
              .append(operationsCsv);
        
        return result.toString();
    }
    
    /**
     * Escapes a field if it contains commas or quotes
     */
    private String escapeField(String field) {
        if (field == null) {
            return "";
        }
        
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        
        return field;
    }
} 