package com.hse.finance.visitor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hse.finance.model.BankAccount;
import com.hse.finance.model.Category;
import com.hse.finance.model.Operation;
import com.hse.finance.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JsonExportVisitorTest {

    private ObjectMapper objectMapper;
    private JsonExportVisitor visitor;
    
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        visitor = new JsonExportVisitor(objectMapper);
    }
    
    @Test
    void visitBankAccounts_ShouldAddAccountsToJson() throws Exception {
        // Given
        List<BankAccount> accounts = new ArrayList<>();
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        
        accounts.add(BankAccount.builder()
                .id(id1)
                .name("Account 1")
                .balance(new BigDecimal("100.00"))
                .build());
                
        accounts.add(BankAccount.builder()
                .id(id2)
                .name("Account 2")
                .balance(new BigDecimal("200.00"))
                .build());
        
        // When
        visitor.visitBankAccounts(accounts);
        String result = visitor.getResult();
        
        // Then
        JsonNode rootNode = objectMapper.readTree(result);
        JsonNode accountsNode = rootNode.get("accounts");
        
        assertNotNull(accountsNode);
        assertEquals(2, accountsNode.size());
        
        assertEquals(id1.toString(), accountsNode.get(0).get("id").asText());
        assertEquals("Account 1", accountsNode.get(0).get("name").asText());
        assertEquals("100.00", accountsNode.get(0).get("balance").asText());
        
        assertEquals(id2.toString(), accountsNode.get(1).get("id").asText());
        assertEquals("Account 2", accountsNode.get(1).get("name").asText());
        assertEquals("200.00", accountsNode.get(1).get("balance").asText());
    }
    
    @Test
    void visitCategories_ShouldAddCategoriesToJson() throws Exception {
        // Given
        List<Category> categories = new ArrayList<>();
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        
        categories.add(Category.builder()
                .id(id1)
                .name("Category 1")
                .type(OperationType.INCOME)
                .build());
                
        categories.add(Category.builder()
                .id(id2)
                .name("Category 2")
                .type(OperationType.EXPENSE)
                .build());
        
        // When
        visitor.visitCategories(categories);
        String result = visitor.getResult();
        
        // Then
        JsonNode rootNode = objectMapper.readTree(result);
        JsonNode categoriesNode = rootNode.get("categories");
        
        assertNotNull(categoriesNode);
        assertEquals(2, categoriesNode.size());
        
        assertEquals(id1.toString(), categoriesNode.get(0).get("id").asText());
        assertEquals("Category 1", categoriesNode.get(0).get("name").asText());
        assertEquals("INCOME", categoriesNode.get(0).get("type").asText());
        
        assertEquals(id2.toString(), categoriesNode.get(1).get("id").asText());
        assertEquals("Category 2", categoriesNode.get(1).get("name").asText());
        assertEquals("EXPENSE", categoriesNode.get(1).get("type").asText());
    }
    
    @Test
    void visitOperations_ShouldAddOperationsToJson() throws Exception {
        // Given
        List<Operation> operations = new ArrayList<>();
        UUID id1 = UUID.randomUUID();
        UUID accountId1 = UUID.randomUUID();
        UUID categoryId1 = UUID.randomUUID();
        LocalDateTime date1 = LocalDateTime.of(2023, 1, 1, 12, 0);
        
        UUID id2 = UUID.randomUUID();
        UUID accountId2 = UUID.randomUUID();
        UUID categoryId2 = UUID.randomUUID();
        LocalDateTime date2 = LocalDateTime.of(2023, 1, 2, 13, 0);
        
        operations.add(Operation.builder()
                .id(id1)
                .type(OperationType.INCOME)
                .bankAccountId(accountId1)
                .amount(new BigDecimal("100.00"))
                .date(date1)
                .categoryId(categoryId1)
                .description("Operation 1")
                .build());
                
        operations.add(Operation.builder()
                .id(id2)
                .type(OperationType.EXPENSE)
                .bankAccountId(accountId2)
                .amount(new BigDecimal("50.00"))
                .date(date2)
                .categoryId(categoryId2)
                .description(null)
                .build());
        
        // When
        visitor.visitOperations(operations);
        String result = visitor.getResult();
        
        // Then
        JsonNode rootNode = objectMapper.readTree(result);
        JsonNode operationsNode = rootNode.get("operations");
        
        assertNotNull(operationsNode);
        assertEquals(2, operationsNode.size());
        
        assertEquals(id1.toString(), operationsNode.get(0).get("id").asText());
        assertEquals("INCOME", operationsNode.get(0).get("type").asText());
        assertEquals(accountId1.toString(), operationsNode.get(0).get("bank_account_id").asText());
        assertEquals("100.00", operationsNode.get(0).get("amount").asText());
        assertEquals(date1.toString(), operationsNode.get(0).get("date").asText());
        assertEquals(categoryId1.toString(), operationsNode.get(0).get("category_id").asText());
        assertEquals("Operation 1", operationsNode.get(0).get("description").asText());
        
        assertEquals(id2.toString(), operationsNode.get(1).get("id").asText());
        assertEquals("EXPENSE", operationsNode.get(1).get("type").asText());
        assertEquals(accountId2.toString(), operationsNode.get(1).get("bank_account_id").asText());
        assertEquals("50.00", operationsNode.get(1).get("amount").asText());
        assertEquals(date2.toString(), operationsNode.get(1).get("date").asText());
        assertEquals(categoryId2.toString(), operationsNode.get(1).get("category_id").asText());
        assertFalse(operationsNode.get(1).has("description"));
    }
    
    @Test
    void getResult_WithEmptyData_ShouldReturnEmptyJsonObject() throws Exception {
        // When
        String result = visitor.getResult();
        
        // Then
        JsonNode rootNode = objectMapper.readTree(result);
        assertTrue(rootNode.isEmpty());
    }
} 