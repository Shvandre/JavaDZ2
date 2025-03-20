package com.hse.finance.service.repository;

import com.hse.finance.model.Operation;
import com.hse.finance.model.OperationType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository for operation data
 */
@Repository
public class OperationRepository {
    private final ConcurrentHashMap<UUID, Operation> operations = new ConcurrentHashMap<>();

    public void save(Operation operation) {
        operations.put(operation.getId(), operation);
    }

    public Optional<Operation> findById(UUID id) {
        return Optional.ofNullable(operations.get(id));
    }

    public List<Operation> findAll() {
        return new ArrayList<>(operations.values());
    }

    public List<Operation> findByBankAccountId(UUID bankAccountId) {
        return operations.values().stream()
                .filter(operation -> operation.getBankAccountId().equals(bankAccountId))
                .collect(Collectors.toList());
    }

    public List<Operation> findByCategoryId(UUID categoryId) {
        return operations.values().stream()
                .filter(operation -> operation.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    public List<Operation> findByType(OperationType type) {
        return operations.values().stream()
                .filter(operation -> operation.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Operation> findByDateBetween(LocalDateTime start, LocalDateTime end) {
        return operations.values().stream()
                .filter(operation -> !operation.getDate().isBefore(start) && !operation.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    public boolean deleteById(UUID id) {
        return operations.remove(id) != null;
    }

    public void deleteByBankAccountId(UUID bankAccountId) {
        operations.values().removeIf(operation -> operation.getBankAccountId().equals(bankAccountId));
    }

    public void deleteByCategoryId(UUID categoryId) {
        operations.values().removeIf(operation -> operation.getCategoryId().equals(categoryId));
    }

    public void clear() {
        operations.clear();
    }
} 