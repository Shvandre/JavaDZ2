package com.hse.finance.visitor;

import com.hse.finance.model.BankAccount;
import com.hse.finance.model.Category;
import com.hse.finance.model.Operation;

import java.util.List;

/**
 * Visitor interface for exporting finance data
 */
public interface ExportVisitor {
    void visitBankAccounts(List<BankAccount> accounts);
    void visitCategories(List<Category> categories);
    void visitOperations(List<Operation> operations);
    String getResult();
} 