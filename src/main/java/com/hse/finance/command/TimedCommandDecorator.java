package com.hse.finance.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decorator that measures execution time of a command
 *
 * @param <T> The result type of the command
 */
public class TimedCommandDecorator<T> implements Command<T> {
    private static final Logger log = LoggerFactory.getLogger(TimedCommandDecorator.class);
    
    private final Command<T> command;
    private final String commandName;

    public TimedCommandDecorator(Command<T> command, String commandName) {
        this.command = command;
        this.commandName = commandName;
    }

    @Override
    public T execute() {
        long startTime = System.currentTimeMillis();
        
        try {
            T result = command.execute();
            long endTime = System.currentTimeMillis();
            log.info("Command {} executed in {} ms", commandName, (endTime - startTime));
            return result;
        } catch (Exception e) {
            long endTime = System.currentTimeMillis();
            log.error("Command {} failed after {} ms: {}", commandName, (endTime - startTime), e.getMessage());
            throw e;
        }
    }
} 