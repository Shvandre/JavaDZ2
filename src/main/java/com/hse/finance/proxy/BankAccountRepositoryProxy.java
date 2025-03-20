package com.hse.finance.proxy;

import com.hse.finance.model.BankAccount;
import com.hse.finance.service.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Proxy for BankAccountRepository that implements caching
 */
@Component
public class BankAccountRepositoryProxy {
    private final BankAccountRepository repository;
    private final ConcurrentHashMap<UUID, BankAccount> cache = new ConcurrentHashMap<>();
    private boolean isCacheInitialized = false;

    @Autowired
    public BankAccountRepositoryProxy(BankAccountRepository repository) {
        this.repository = repository;
    }

    private void initCacheIfNeeded() {
        if (!isCacheInitialized) {
            repository.findAll().forEach(account -> cache.put(account.getId(), account));
            isCacheInitialized = true;
        }
    }

    public void save(BankAccount account) {
        initCacheIfNeeded();
        repository.save(account);
        cache.put(account.getId(), account);
    }

    public Optional<BankAccount> findById(UUID id) {
        initCacheIfNeeded();
        return Optional.ofNullable(cache.get(id));
    }

    public List<BankAccount> findAll() {
        initCacheIfNeeded();
        return List.copyOf(cache.values());
    }

    public boolean deleteById(UUID id) {
        initCacheIfNeeded();
        boolean isDeleted = repository.deleteById(id);
        if (isDeleted) {
            cache.remove(id);
        }
        return isDeleted;
    }

    public void clear() {
        repository.clear();
        cache.clear();
        isCacheInitialized = false;
    }
} 