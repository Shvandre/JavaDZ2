package com.hse.finance.service.repository;

import com.hse.finance.model.Category;
import com.hse.finance.model.OperationType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for category data
 */
@Repository
public class CategoryRepository {
    private final ConcurrentHashMap<UUID, Category> categories = new ConcurrentHashMap<>();

    public void save(Category category) {
        categories.put(category.getId(), category);
    }

    public Optional<Category> findById(UUID id) {
        return Optional.ofNullable(categories.get(id));
    }

    public List<Category> findAll() {
        return new ArrayList<>(categories.values());
    }

    public List<Category> findByType(OperationType type) {
        return categories.values().stream()
                .filter(category -> category.getType() == type)
                .collect(Collectors.toList());
    }

    public boolean deleteById(UUID id) {
        return categories.remove(id) != null;
    }

    public void clear() {
        categories.clear();
    }
} 