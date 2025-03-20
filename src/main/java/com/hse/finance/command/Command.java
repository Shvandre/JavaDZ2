package com.hse.finance.command;

/**
 * Command interface for Command pattern
 */
public interface Command<T> {
    /**
     * Executes the command and returns the result
     */
    T execute();
} 