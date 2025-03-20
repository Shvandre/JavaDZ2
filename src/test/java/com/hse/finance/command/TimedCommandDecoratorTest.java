package com.hse.finance.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TimedCommandDecoratorTest {

    @Test
    void execute_ShouldReturnResultFromDecoratedCommand() {
        // Given
        String expectedResult = "Command executed";
        Command<String> mockCommand = () -> expectedResult;
        TimedCommandDecorator<String> decorator = new TimedCommandDecorator<>(mockCommand, "TestCommand");
        
        // When
        String result = decorator.execute();
        
        // Then
        assertEquals(expectedResult, result);
    }
    
    @Test
    void execute_ShouldExecuteDecoratedCommand() {
        // Given
        boolean[] commandExecuted = {false};
        Command<Void> mockCommand = () -> {
            commandExecuted[0] = true;
            return null;
        };
        TimedCommandDecorator<Void> decorator = new TimedCommandDecorator<>(mockCommand, "TestCommand");
        
        // When
        decorator.execute();
        
        // Then
        assertTrue(commandExecuted[0]);
    }
    
    @Test
    void execute_WithException_ShouldPropagateException() {
        // Given
        RuntimeException expectedException = new RuntimeException("Test exception");
        Command<Void> mockCommand = () -> {
            throw expectedException;
        };
        TimedCommandDecorator<Void> decorator = new TimedCommandDecorator<>(mockCommand, "TestCommand");
        
        // When & Then
        RuntimeException actualException = assertThrows(RuntimeException.class, decorator::execute);
        assertSame(expectedException, actualException);
    }
} 