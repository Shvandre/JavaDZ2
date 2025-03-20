package com.hse.finance.visitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.hse.finance.model.BankAccount;
import com.hse.finance.model.Category;
import com.hse.finance.model.Operation;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Visitor for exporting data to YAML format
 */
@Component
public class YamlExportVisitor implements ExportVisitor {
    private final ObjectMapper yamlMapper;
    private ObjectNode rootNode;

    public YamlExportVisitor() {
        this.yamlMapper = new ObjectMapper(new YAMLFactory());
        this.rootNode = yamlMapper.createObjectNode();
    }

    @Override
    public void visitBankAccounts(List<BankAccount> accounts) {
        ArrayNode accountsNode = yamlMapper.createArrayNode();
        
        for (BankAccount account : accounts) {
            ObjectNode accountNode = yamlMapper.createObjectNode();
            accountNode.put("id", account.getId().toString());
            accountNode.put("name", account.getName());
            accountNode.put("balance", account.getBalance().toString());
            accountsNode.add(accountNode);
        }
        
        rootNode.set("accounts", accountsNode);
    }

    @Override
    public void visitCategories(List<Category> categories) {
        ArrayNode categoriesNode = yamlMapper.createArrayNode();
        
        for (Category category : categories) {
            ObjectNode categoryNode = yamlMapper.createObjectNode();
            categoryNode.put("id", category.getId().toString());
            categoryNode.put("name", category.getName());
            categoryNode.put("type", category.getType().name());
            categoriesNode.add(categoryNode);
        }
        
        rootNode.set("categories", categoriesNode);
    }

    @Override
    public void visitOperations(List<Operation> operations) {
        ArrayNode operationsNode = yamlMapper.createArrayNode();
        
        for (Operation operation : operations) {
            ObjectNode operationNode = yamlMapper.createObjectNode();
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
            return yamlMapper.writeValueAsString(rootNode);
        } catch (Exception e) {
            return "{}";
        }
    }
} 