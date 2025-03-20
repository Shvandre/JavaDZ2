package com.hse.finance.console;

import com.hse.finance.facade.*;
import com.hse.finance.model.BankAccount;
import com.hse.finance.model.Category;
import com.hse.finance.model.Operation;
import com.hse.finance.model.OperationType;
import com.hse.finance.util.importer.CsvDataImporter;
import com.hse.finance.util.importer.JsonDataImporter;
import com.hse.finance.util.importer.YamlDataImporter;
import com.hse.finance.visitor.CsvExportVisitor;
import com.hse.finance.visitor.JsonExportVisitor;
import com.hse.finance.visitor.YamlExportVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

/**
 * Console application for demonstrating finance functionality
 */
@Component
public class ConsoleApplication implements CommandLineRunner {
    private BankAccountFacade bankAccountFacade;
    private CategoryFacade categoryFacade;
    private OperationFacade operationFacade;
    private AnalyticsFacade analyticsFacade;
    private ExportFacade exportFacade;
    private JsonDataImporter jsonDataImporter;
    private YamlDataImporter yamlDataImporter;
    private CsvDataImporter csvDataImporter;
    private JsonExportVisitor jsonExportVisitor;
    private YamlExportVisitor yamlExportVisitor;
    private CsvExportVisitor csvExportVisitor;
    
    private final Scanner scanner = new Scanner(System.in);
    
    @Autowired
    public ConsoleApplication(BankAccountFacade bankAccountFacade, CategoryFacade categoryFacade,
                             OperationFacade operationFacade, AnalyticsFacade analyticsFacade,
                             ExportFacade exportFacade, JsonDataImporter jsonDataImporter,
                             YamlDataImporter yamlDataImporter, CsvDataImporter csvDataImporter,
                             JsonExportVisitor jsonExportVisitor, YamlExportVisitor yamlExportVisitor,
                             CsvExportVisitor csvExportVisitor) {
        this.bankAccountFacade = bankAccountFacade;
        this.categoryFacade = categoryFacade;
        this.operationFacade = operationFacade;
        this.analyticsFacade = analyticsFacade;
        this.exportFacade = exportFacade;
        this.jsonDataImporter = jsonDataImporter;
        this.yamlDataImporter = yamlDataImporter;
        this.csvDataImporter = csvDataImporter;
        this.jsonExportVisitor = jsonExportVisitor;
        this.yamlExportVisitor = yamlExportVisitor;
        this.csvExportVisitor = csvExportVisitor;
    }
    
    @Override
    public void run(String... args) {
        boolean running = true;
        
        System.out.println("Welcome to HSE Finance App!");
        
        while (running) {
            printMainMenu();
            int choice = readIntInput();
            
            try {
                switch (choice) {
                    case 1:
                        manageBankAccounts();
                        break;
                    case 2:
                        manageCategories();
                        break;
                    case 3:
                        manageOperations();
                        break;
                    case 4:
                        runAnalytics();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        importData();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Thank you for using HSE Finance App!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void printMainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Manage Bank Accounts");
        System.out.println("2. Manage Categories");
        System.out.println("3. Manage Operations");
        System.out.println("4. Analytics");
        System.out.println("5. Export Data");
        System.out.println("6. Import Data");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private void manageBankAccounts() {
        boolean managingAccounts = true;
        
        while (managingAccounts) {
            System.out.println("\n===== BANK ACCOUNTS =====");
            System.out.println("1. List All Accounts");
            System.out.println("2. Create Account");
            System.out.println("3. Update Account");
            System.out.println("4. Delete Account");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = readIntInput();
            
            try {
                switch (choice) {
                    case 1:
                        listAllAccounts();
                        break;
                    case 2:
                        createAccount();
                        break;
                    case 3:
                        updateAccount();
                        break;
                    case 4:
                        deleteAccount();
                        break;
                    case 0:
                        managingAccounts = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void listAllAccounts() {
        List<BankAccount> accounts = bankAccountFacade.getAllBankAccounts();
        
        if (accounts.isEmpty()) {
            System.out.println("No bank accounts found.");
            return;
        }
        
        System.out.println("\nBank Accounts:");
        for (BankAccount account : accounts) {
            System.out.println(account.getId() + " | " + account.getName() + " | Balance: " + account.getBalance());
        }
    }
    
    private void createAccount() {
        System.out.print("Enter account name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter initial balance (default 0): ");
        String balanceStr = scanner.nextLine();
        
        BigDecimal balance = balanceStr.isEmpty() ? 
                BigDecimal.ZERO : new BigDecimal(balanceStr);
        
        BankAccount account = bankAccountFacade.createBankAccount(name, balance);
        System.out.println("Account created successfully with ID: " + account.getId());
    }
    
    private void updateAccount() {
        listAllAccounts();
        
        System.out.print("Enter account ID to update: ");
        String idStr = scanner.nextLine();
        UUID id = UUID.fromString(idStr);
        
        System.out.print("Enter new account name: ");
        String name = scanner.nextLine();
        
        bankAccountFacade.updateBankAccount(id, name);
        System.out.println("Account updated successfully.");
    }
    
    private void deleteAccount() {
        listAllAccounts();
        
        System.out.print("Enter account ID to delete: ");
        String idStr = scanner.nextLine();
        UUID id = UUID.fromString(idStr);
        
        boolean deleted = bankAccountFacade.deleteBankAccount(id);
        
        if (deleted) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("Account not found or could not be deleted.");
        }
    }
    
    private void manageCategories() {
        boolean managingCategories = true;
        
        while (managingCategories) {
            System.out.println("\n===== CATEGORIES =====");
            System.out.println("1. List All Categories");
            System.out.println("2. Create Category");
            System.out.println("3. Update Category");
            System.out.println("4. Delete Category");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = readIntInput();
            
            try {
                switch (choice) {
                    case 1:
                        listAllCategories();
                        break;
                    case 2:
                        createCategory();
                        break;
                    case 3:
                        updateCategory();
                        break;
                    case 4:
                        deleteCategory();
                        break;
                    case 0:
                        managingCategories = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void listAllCategories() {
        List<Category> categories = categoryFacade.getAllCategories();
        
        if (categories.isEmpty()) {
            System.out.println("No categories found.");
            return;
        }
        
        System.out.println("\nCategories:");
        for (Category category : categories) {
            System.out.println(category.getId() + " | " + category.getName() + " | Type: " + category.getType());
        }
    }
    
    private void createCategory() {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine();
        
        System.out.println("Select category type:");
        System.out.println("1. INCOME");
        System.out.println("2. EXPENSE");
        System.out.print("Enter choice (1 or 2): ");
        
        int typeChoice = readIntInput();
        OperationType type = (typeChoice == 1) ? OperationType.INCOME : OperationType.EXPENSE;
        
        Category category = categoryFacade.createCategory(name, type);
        System.out.println("Category created successfully with ID: " + category.getId());
    }
    
    private void updateCategory() {
        listAllCategories();
        
        System.out.print("Enter category ID to update: ");
        String idStr = scanner.nextLine();
        UUID id = UUID.fromString(idStr);
        
        System.out.print("Enter new category name: ");
        String name = scanner.nextLine();
        
        categoryFacade.updateCategory(id, name);
        System.out.println("Category updated successfully.");
    }
    
    private void deleteCategory() {
        listAllCategories();
        
        System.out.print("Enter category ID to delete: ");
        String idStr = scanner.nextLine();
        UUID id = UUID.fromString(idStr);
        
        boolean deleted = categoryFacade.deleteCategory(id);
        
        if (deleted) {
            System.out.println("Category deleted successfully.");
        } else {
            System.out.println("Category not found or could not be deleted.");
        }
    }
    
    private void manageOperations() {
        boolean managingOperations = true;
        
        while (managingOperations) {
            System.out.println("\n===== OPERATIONS =====");
            System.out.println("1. List All Operations");
            System.out.println("2. Create Operation");
            System.out.println("3. Delete Operation");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = readIntInput();
            
            try {
                switch (choice) {
                    case 1:
                        listAllOperations();
                        break;
                    case 2:
                        createOperation();
                        break;
                    case 3:
                        deleteOperation();
                        break;
                    case 0:
                        managingOperations = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void listAllOperations() {
        List<Operation> operations = operationFacade.getAllOperations();
        
        if (operations.isEmpty()) {
            System.out.println("No operations found.");
            return;
        }
        
        System.out.println("\nOperations:");
        for (Operation operation : operations) {
            System.out.println(operation.getId() + " | " + operation.getType() + 
                    " | Amount: " + operation.getAmount() +
                    " | Date: " + operation.getDate() +
                    " | Description: " + operation.getDescription());
        }
    }
    
    private void createOperation() {
        // List accounts and categories for reference
        listAllAccounts();
        listAllCategories();
        
        // Select operation type
        System.out.println("Select operation type:");
        System.out.println("1. INCOME");
        System.out.println("2. EXPENSE");
        System.out.print("Enter choice (1 or 2): ");
        
        int typeChoice = readIntInput();
        OperationType type = (typeChoice == 1) ? OperationType.INCOME : OperationType.EXPENSE;
        
        // Get account ID
        System.out.print("Enter bank account ID: ");
        String accountIdStr = scanner.nextLine();
        UUID accountId = UUID.fromString(accountIdStr);
        
        // Get amount
        System.out.print("Enter amount: ");
        String amountStr = scanner.nextLine();
        BigDecimal amount = new BigDecimal(amountStr);
        
        // Get category ID
        System.out.print("Enter category ID: ");
        String categoryIdStr = scanner.nextLine();
        UUID categoryId = UUID.fromString(categoryIdStr);
        
        // Get description
        System.out.print("Enter description (optional): ");
        String description = scanner.nextLine();
        
        Operation operation = operationFacade.createOperation(type, accountId, amount, categoryId, description);
        System.out.println("Operation created successfully with ID: " + operation.getId());
    }
    
    private void deleteOperation() {
        listAllOperations();
        
        System.out.print("Enter operation ID to delete: ");
        String idStr = scanner.nextLine();
        UUID id = UUID.fromString(idStr);
        
        boolean deleted = operationFacade.deleteOperation(id);
        
        if (deleted) {
            System.out.println("Operation deleted successfully.");
        } else {
            System.out.println("Operation not found or could not be deleted.");
        }
    }
    
    private void runAnalytics() {
        boolean runningAnalytics = true;
        
        while (runningAnalytics) {
            System.out.println("\n===== ANALYTICS =====");
            System.out.println("1. Calculate Balance Difference");
            System.out.println("2. Group Operations by Category");
            System.out.println("3. Top Spending Categories");
            System.out.println("4. Monthly Trend for Category");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = readIntInput();
            
            try {
                switch (choice) {
                    case 1:
                        calculateBalanceDifference();
                        break;
                    case 2:
                        groupOperationsByCategory();
                        break;
                    case 3:
                        getTopSpendingCategories();
                        break;
                    case 4:
                        getMonthlyTrendForCategory();
                        break;
                    case 0:
                        runningAnalytics = false;
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    private void calculateBalanceDifference() {
        LocalDateTime startDate = getDateInput("Enter start date (yyyy-MM-dd): ");
        LocalDateTime endDate = getDateInput("Enter end date (yyyy-MM-dd): ");
        
        BigDecimal difference = analyticsFacade.calculateBalanceDifference(startDate, endDate);
        
        System.out.println("Balance difference between " + startDate.toLocalDate() + 
                " and " + endDate.toLocalDate() + " is: " + difference);
    }
    
    private void groupOperationsByCategory() {
        LocalDateTime startDate = getDateInput("Enter start date (yyyy-MM-dd): ");
        LocalDateTime endDate = getDateInput("Enter end date (yyyy-MM-dd): ");
        
        Map<Category, BigDecimal> categoryTotals = analyticsFacade.groupOperationsByCategory(startDate, endDate);
        
        System.out.println("Category totals between " + startDate.toLocalDate() + 
                " and " + endDate.toLocalDate() + ":");
                
        for (Map.Entry<Category, BigDecimal> entry : categoryTotals.entrySet()) {
            System.out.println(entry.getKey().getName() + " (" + entry.getKey().getType() + "): " + entry.getValue());
        }
    }
    
    private void getTopSpendingCategories() {
        LocalDateTime startDate = getDateInput("Enter start date (yyyy-MM-dd): ");
        LocalDateTime endDate = getDateInput("Enter end date (yyyy-MM-dd): ");
        
        System.out.print("Enter number of top categories to show: ");
        int limit = readIntInput();
        
        List<Map.Entry<Category, BigDecimal>> topCategories = 
                analyticsFacade.getTopSpendingCategories(startDate, endDate, limit);
        
        System.out.println("Top " + limit + " spending categories between " + 
                startDate.toLocalDate() + " and " + endDate.toLocalDate() + ":");
                
        int rank = 1;
        for (Map.Entry<Category, BigDecimal> entry : topCategories) {
            System.out.println(rank + ". " + entry.getKey().getName() + ": " + entry.getValue());
            rank++;
        }
    }
    
    private void getMonthlyTrendForCategory() {
        listAllCategories();
        
        System.out.print("Enter category ID: ");
        String categoryIdStr = scanner.nextLine();
        UUID categoryId = UUID.fromString(categoryIdStr);
        
        System.out.print("Enter number of months to analyze: ");
        int months = readIntInput();
        
        Map<String, BigDecimal> monthlyTrend = analyticsFacade.getMonthlyTrendForCategory(categoryId, months);
        
        System.out.println("Monthly trend for selected category:");
        for (Map.Entry<String, BigDecimal> entry : monthlyTrend.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    
    private void exportData() {
        System.out.println("\n===== EXPORT DATA =====");
        System.out.println("Select export format:");
        System.out.println("1. JSON");
        System.out.println("2. YAML");
        System.out.println("3. CSV");
        System.out.print("Enter choice: ");
        
        int formatChoice = readIntInput();
        
        System.out.print("Enter output file path: ");
        String filePath = scanner.nextLine();
        
        try {
            switch (formatChoice) {
                case 1:
                    exportFacade.exportData(jsonExportVisitor, filePath);
                    break;
                case 2:
                    exportFacade.exportData(yamlExportVisitor, filePath);
                    break;
                case 3:
                    exportFacade.exportData(csvExportVisitor, filePath);
                    break;
                default:
                    System.out.println("Invalid format choice.");
                    return;
            }
            
            System.out.println("Data exported successfully to: " + filePath);
        } catch (Exception e) {
            System.out.println("Error exporting data: " + e.getMessage());
        }
    }
    
    private void importData() {
        System.out.println("\n===== IMPORT DATA =====");
        System.out.println("Select import format:");
        System.out.println("1. JSON");
        System.out.println("2. YAML");
        System.out.println("3. CSV");
        System.out.print("Enter choice: ");
        
        int formatChoice = readIntInput();
        
        System.out.print("Enter input file path: ");
        String filePath = scanner.nextLine();
        
        try {
            File file = new File(filePath);
            
            switch (formatChoice) {
                case 1:
                    jsonDataImporter.importData(file);
                    break;
                case 2:
                    yamlDataImporter.importData(file);
                    break;
                case 3:
                    csvDataImporter.importData(file);
                    break;
                default:
                    System.out.println("Invalid format choice.");
                    return;
            }
            
            System.out.println("Data imported successfully from: " + filePath);
        } catch (Exception e) {
            System.out.println("Error importing data: " + e.getMessage());
        }
    }
    
    private int readIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private LocalDateTime getDateInput(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                
                LocalDateTime dateTime = LocalDateTime.parse(input + "T00:00:00");
                return dateTime;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }
} 