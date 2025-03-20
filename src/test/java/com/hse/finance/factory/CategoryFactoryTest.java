package com.hse.finance.factory;

import com.hse.finance.model.Category;
import com.hse.finance.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryFactoryTest {
    
    private CategoryFactory factory;
    
    @BeforeEach
    void setUp() {
        factory = new CategoryFactory();
    }
    
    @Test
    void createCategory_WithValidParameters_ShouldCreateCategory() {
        // Given
        String name = "Test Category";
        OperationType type = OperationType.INCOME;
        
        // When
        Category category = factory.createCategory(name, type);
        
        // Then
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(name, category.getName());
        assertEquals(type, category.getType());
    }
    
    @Test
    void createCategory_WithNullName_ShouldThrowException() {
        // Given
        String name = null;
        OperationType type = OperationType.EXPENSE;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCategory(name, type);
        });
    }
    
    @Test
    void createCategory_WithEmptyName_ShouldThrowException() {
        // Given
        String name = "";
        OperationType type = OperationType.EXPENSE;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCategory(name, type);
        });
    }
    
    @Test
    void createCategory_WithNullType_ShouldThrowException() {
        // Given
        String name = "Test Category";
        OperationType type = null;
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            factory.createCategory(name, type);
        });
    }
} 