package com.hse.finance.factory;

import com.hse.finance.model.Category;
import com.hse.finance.model.OperationType;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Factory for creating Category objects
 */
@Component
public class CategoryFactory {

    /**
     * Creates a new category with validated parameters
     */
    public Category createCategory(String name, OperationType type) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        
        if (type == null) {
            throw new IllegalArgumentException("Category type cannot be null");
        }
        
        return Category.builder()
                .id(UUID.randomUUID())
                .name(name)
                .type(type)
                .build();
    }
} 