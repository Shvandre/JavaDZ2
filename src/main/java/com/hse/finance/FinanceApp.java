package com.hse.finance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Main entry point for the Finance application
 */
@SpringBootApplication
public class FinanceApp {
    
    public static void main(String[] args) {
        SpringApplication.run(FinanceApp.class, args);
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
} 