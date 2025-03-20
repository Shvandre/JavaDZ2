package com.hse.finance.service.repository;

import com.hse.finance.model.BankAccount;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository for bank account data
 */
@Repository
public class BankAccountRepository {
    private final ConcurrentHashMap<UUID, BankAccount> accounts = new ConcurrentHashMap<>();

    public void save(BankAccount account) {
        accounts.put(account.getId(), account);
    }

    public Optional<BankAccount> findById(UUID id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public List<BankAccount> findAll() {
        return new ArrayList<>(accounts.values());
    }

    public boolean deleteById(UUID id) {
        return accounts.remove(id) != null;
    }

    public void clear() {
        accounts.clear();
    }
} 