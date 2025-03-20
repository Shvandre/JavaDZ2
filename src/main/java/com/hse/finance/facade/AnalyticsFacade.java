package com.hse.finance.facade;

import com.hse.finance.model.Category;
import com.hse.finance.model.Operation;
import com.hse.finance.model.OperationType;
import com.hse.finance.service.repository.CategoryRepository;
import com.hse.finance.service.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Facade for analytics operations
 */
@Service
public class AnalyticsFacade {
    private OperationRepository operationRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public AnalyticsFacade(OperationRepository operationRepository, CategoryRepository categoryRepository) {
        this.operationRepository = operationRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Calculate the difference between income and expenses in the specified period
     */
    public BigDecimal calculateBalanceDifference(LocalDateTime startDate, LocalDateTime endDate) {
        List<Operation> operations = operationRepository.findByDateBetween(startDate, endDate);
        
        BigDecimal income = operations.stream()
                .filter(op -> op.getType() == OperationType.INCOME)
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        BigDecimal expenses = operations.stream()
                .filter(op -> op.getType() == OperationType.EXPENSE)
                .map(Operation::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        return income.subtract(expenses);
    }

    /**
     * Group operations by category and calculate sum for each category
     */
    public Map<Category, BigDecimal> groupOperationsByCategory(LocalDateTime startDate, LocalDateTime endDate) {
        List<Operation> operations = operationRepository.findByDateBetween(startDate, endDate);
        Map<UUID, BigDecimal> categoryAmounts = new HashMap<>();
        
        // Group and sum amounts by category ID
        for (Operation operation : operations) {
            UUID categoryId = operation.getCategoryId();
            BigDecimal amount = operation.getAmount();
            
            categoryAmounts.put(categoryId, 
                    categoryAmounts.getOrDefault(categoryId, BigDecimal.ZERO).add(amount));
        }
        
        // Map category IDs to actual Category objects
        Map<Category, BigDecimal> result = new HashMap<>();
        for (Map.Entry<UUID, BigDecimal> entry : categoryAmounts.entrySet()) {
            categoryRepository.findById(entry.getKey()).ifPresent(category -> 
                    result.put(category, entry.getValue()));
        }
        
        return result;
    }

    /**
     * Get top spending categories in the specified period
     */
    public List<Map.Entry<Category, BigDecimal>> getTopSpendingCategories(
            LocalDateTime startDate, LocalDateTime endDate, int limit) {
            
        List<Operation> operations = operationRepository.findByDateBetween(startDate, endDate)
                .stream()
                .filter(op -> op.getType() == OperationType.EXPENSE)
                .collect(Collectors.toList());
                
        Map<UUID, BigDecimal> categoryAmounts = new HashMap<>();
        
        // Group and sum amounts by category ID
        for (Operation operation : operations) {
            UUID categoryId = operation.getCategoryId();
            BigDecimal amount = operation.getAmount();
            
            categoryAmounts.put(categoryId, 
                    categoryAmounts.getOrDefault(categoryId, BigDecimal.ZERO).add(amount));
        }
        
        // Map category IDs to actual Category objects
        Map<Category, BigDecimal> categoryTotals = new HashMap<>();
        for (Map.Entry<UUID, BigDecimal> entry : categoryAmounts.entrySet()) {
            categoryRepository.findById(entry.getKey()).ifPresent(category -> 
                    categoryTotals.put(category, entry.getValue()));
        }
        
        // Sort by amount (descending) and limit
        return categoryTotals.entrySet().stream()
                .sorted(Map.Entry.<Category, BigDecimal>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Get month-to-month spending comparison for a given category
     */
    public Map<String, BigDecimal> getMonthlyTrendForCategory(UUID categoryId, int monthsToAnalyze) {
        // Get the category to ensure it exists
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
        
        // Calculate start date (N months ago)
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(monthsToAnalyze);
        
        // Get operations for this category in the date range
        List<Operation> operations = operationRepository.findByCategoryId(categoryId).stream()
                .filter(op -> !op.getDate().isBefore(startDate) && !op.getDate().isAfter(endDate))
                .collect(Collectors.toList());
        
        // Group operations by month and calculate total for each month
        Map<String, BigDecimal> monthlyTotals = new TreeMap<>(); // TreeMap for chronological order
        
        for (Operation operation : operations) {
            String monthKey = operation.getDate().getYear() + "-" + 
                    String.format("%02d", operation.getDate().getMonthValue());
            
            monthlyTotals.put(monthKey, 
                    monthlyTotals.getOrDefault(monthKey, BigDecimal.ZERO)
                    .add(operation.getAmount()));
        }
        
        return monthlyTotals;
    }
} 