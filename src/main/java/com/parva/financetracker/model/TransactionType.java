package com.parva.financetracker.model;

public enum TransactionType {
    INCOME("Income"),
    EXPENSE("Expense");
    
    private final String displayName;
    
    TransactionType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}