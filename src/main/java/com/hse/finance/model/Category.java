package com.hse.finance.model;

import java.util.UUID;

/**
 * Represents a category entity for operations
 */
public class Category {
    private UUID id;
    private String name;
    private OperationType type;

    // Default constructor
    public Category() {
    }

    // All args constructor
    public Category(UUID id, String name, OperationType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    // Builder pattern implementation
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private UUID id;
        private String name;
        private OperationType type;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(OperationType type) {
            this.type = type;
            return this;
        }

        public Category build() {
            return new Category(id, name, type);
        }
    }
} 