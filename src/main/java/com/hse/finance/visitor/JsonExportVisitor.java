package com.hse.finance.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hse.finance.model.BankAccount;
import com.hse.finance.model.Category;
import com.hse.finance.model.Operation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Visitor for exporting data to JSON format
 */
@Component
public class JsonExportVisitor implements ExportVisitor {
    private final ObjectMapper objectMapper;
    private ObjectNode rootNode;

    public JsonExportVisitor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.rootNode = objectMapper.createObjectNode();
    }

    @Override
    public void visitBankAccounts(List<BankAccount> accounts) {
        ArrayNode accountsNode = objectMapper.createArrayNode();
        
        for (BankAccount account : accounts) {
            ObjectNode accountNode = objectMapper.createObjectNode();
            accountNode.put("id", account.getId().toString());
            accountNode.put("name", account.getName());
            accountNode.put("balance", account.getBalance().toString());
            accountsNode.add(accountNode);
        }
        
        rootNode.set("accounts", accountsNode);
    }

    @Override
    public void visitCategories(List<Category> categories) {
        ArrayNode categoriesNode = objectMapper.createArrayNode();
        
        for (Category category : categories) {
            ObjectNode categoryNode = objectMapper.createObjectNode();
            categoryNode.put("id", category.getId().toString());
            categoryNode.put("name", category.getName());
            categoryNode.put("type", category.getType().name());
            categoriesNode.add(categoryNode);
        }
        
        rootNode.set("categories", categoriesNode);
    }

    @Override
    public void visitOperations(List<Operation> operations) {
        ArrayNode operationsNode = objectMapper.createArrayNode();
        
        for (Operation operation : operations) {
            ObjectNode operationNode = objectMapper.createObjectNode();
            operationNode.put("id", operation.getId().toString());
            operationNode.put("type", operation.getType().name());
            operationNode.put("amount", operation.getAmount().toString());
            operationNode.put("date", operation.getDate().toString());
            operationNode.put("bank_account_id", operation.getBankAccountId().toString());
            operationNode.put("category_id", operation.getCategoryId().toString());
            
            if (operation.getDescription() != null) {
                operationNode.put("description", operation.getDescription());
            }
            
            operationsNode.add(operationNode);
        }
        
        rootNode.set("operations", operationsNode);
    }

    @Override
    public String getResult() {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
        } catch (Exception e) {
            return "{}";
        }
    }
} 