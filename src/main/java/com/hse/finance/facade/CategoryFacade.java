package com.hse.finance.facade;

import com.hse.finance.factory.CategoryFactory;
import com.hse.finance.model.Category;
import com.hse.finance.model.OperationType;
import com.hse.finance.service.repository.CategoryRepository;
import com.hse.finance.service.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Facade for category operations
 */
@Service
public class CategoryFacade {
    private CategoryFactory categoryFactory;
    private CategoryRepository categoryRepository;
    private OperationRepository operationRepository;

    @Autowired
    public CategoryFacade(CategoryFactory categoryFactory, CategoryRepository categoryRepository, 
                         OperationRepository operationRepository) {
        this.categoryFactory = categoryFactory;
        this.categoryRepository = categoryRepository;
        this.operationRepository = operationRepository;
    }

    public Category createCategory(String name, OperationType type) {
        Category category = categoryFactory.createCategory(name, type);
        categoryRepository.save(category);
        return category;
    }

    public Optional<Category> updateCategory(UUID categoryId, String newName) {
        return categoryRepository.findById(categoryId).map(category -> {
            category.setName(newName);
            categoryRepository.save(category);
            return category;
        });
    }

    public boolean deleteCategory(UUID categoryId) {
        if (!categoryRepository.findById(categoryId).isPresent()) {
            return false;
        }

        // Delete all operations with this category first
        operationRepository.deleteByCategoryId(categoryId);
        
        return categoryRepository.deleteById(categoryId);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getCategoriesByType(OperationType type) {
        return categoryRepository.findByType(type);
    }

    public Optional<Category> getCategoryById(UUID id) {
        return categoryRepository.findById(id);
    }
} 