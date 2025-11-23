package com.parva.financetracker.service;

import com.parva.financetracker.dto.*;
import com.parva.financetracker.exception.ResourceNotFoundException;
import com.parva.financetracker.model.TransactionType;
import com.parva.financetracker.model.User;
import com.parva.financetracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private BudgetRepository budgetRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public MonthlySummaryResponse getMonthlySummary(String username, Integer month, Integer year) {
        User user = getUserByUsername(username);
        
        // Get total income for the month
        BigDecimal totalIncome = transactionRepository.sumAmountByUserIdAndTypeAndMonthAndYear(
                user.getId(), TransactionType.INCOME, month, year);
        if (totalIncome == null) totalIncome = BigDecimal.ZERO;
        
        // Get total expenses for the month
        BigDecimal totalExpenses = transactionRepository.sumAmountByUserIdAndTypeAndMonthAndYear(
                user.getId(), TransactionType.EXPENSE, month, year);
        if (totalExpenses == null) totalExpenses = BigDecimal.ZERO;
        
        // Calculate savings
        BigDecimal savings = totalIncome.subtract(totalExpenses);
        
        // Get total budget allocated
        BigDecimal budgetAllocated = budgetRepository.sumBudgetAmountByUserIdAndMonthAndYear(
                user.getId(), month, year);
        if (budgetAllocated == null) budgetAllocated = BigDecimal.ZERO;
        
        // Calculate budget utilization
        Double budgetUtilization = 0.0;
        if (budgetAllocated.compareTo(BigDecimal.ZERO) > 0) {
            budgetUtilization = totalExpenses.divide(budgetAllocated, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).doubleValue();
        }
        
        return MonthlySummaryResponse.builder()
                .month(month)
                .year(year)
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .savings(savings)
                .budgetAllocated(budgetAllocated)
                .budgetUsed(totalExpenses)
                .budgetUtilizationPercentage(budgetUtilization)
                .build();
    }
    
    public List<CategoryBreakdownResponse> getCategoryBreakdown(
            String username, Integer month, Integer year, TransactionType type) {
        User user = getUserByUsername(username);
        
        List<CategoryBreakdownResponse> breakdown = new ArrayList<>();
        
        // Get total for percentage calculation
        BigDecimal total = transactionRepository.sumAmountByUserIdAndTypeAndMonthAndYear(
                user.getId(), type, month, year);
        if (total == null || total.compareTo(BigDecimal.ZERO) == 0) {
            return breakdown;
        }
        
        // Get breakdown by category
        List<Object[]> results = transactionRepository.getCategoryBreakdown(
                user.getId(), type, month, year);
        
        for (Object[] result : results) {
            Long categoryId = ((Number) result[0]).longValue();
            String categoryName = (String) result[1];
            String categoryIcon = (String) result[2];
            BigDecimal amount = (BigDecimal) result[3];
            Long count = ((Number) result[4]).longValue();
            
            Double percentage = amount.divide(total, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).doubleValue();
            
            breakdown.add(CategoryBreakdownResponse.builder()
                    .categoryId(categoryId)
                    .categoryName(categoryName)
                    .categoryIcon(categoryIcon)
                    .totalAmount(amount)
                    .transactionCount(count.intValue())
                    .percentageOfTotal(percentage)
                    .build());
        }
        
        return breakdown;
    }
    
    public List<MonthlyComparisonResponse> getMonthlyComparison(
            String username, Integer numberOfMonths) {
        User user = getUserByUsername(username);
        List<MonthlyComparisonResponse> comparison = new ArrayList<>();
        
        YearMonth currentMonth = YearMonth.now();
        
        for (int i = numberOfMonths - 1; i >= 0; i--) {
            YearMonth targetMonth = currentMonth.minusMonths(i);
            int month = targetMonth.getMonthValue();
            int year = targetMonth.getYear();
            
            BigDecimal income = transactionRepository.sumAmountByUserIdAndTypeAndMonthAndYear(
                    user.getId(), TransactionType.INCOME, month, year);
            if (income == null) income = BigDecimal.ZERO;
            
            BigDecimal expenses = transactionRepository.sumAmountByUserIdAndTypeAndMonthAndYear(
                    user.getId(), TransactionType.EXPENSE, month, year);
            if (expenses == null) expenses = BigDecimal.ZERO;
            
            BigDecimal savings = income.subtract(expenses);
            
            comparison.add(MonthlyComparisonResponse.builder()
                    .month(month)
                    .year(year)
                    .monthName(Month.of(month).name())
                    .totalIncome(income)
                    .totalExpenses(expenses)
                    .savings(savings)
                    .build());
        }
        
        return comparison;
    }
    
    public List<BudgetUtilizationResponse> getBudgetUtilization(
            String username, Integer month, Integer year) {
        User user = getUserByUsername(username);
        List<BudgetUtilizationResponse> utilization = new ArrayList<>();
        
        List<Object[]> results = budgetRepository.getBudgetUtilization(user.getId(), month, year);
        
        for (Object[] result : results) {
            Long categoryId = ((Number) result[0]).longValue();
            String categoryName = (String) result[1];
            BigDecimal budgetAmount = (BigDecimal) result[2];
            BigDecimal spentAmount = (BigDecimal) result[3];
            if (spentAmount == null) spentAmount = BigDecimal.ZERO;
            
            BigDecimal remaining = budgetAmount.subtract(spentAmount);
            Double percentage = spentAmount.divide(budgetAmount, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)).doubleValue();
            
            String status;
            if (percentage >= 100) {
                status = "EXCEEDED";
            } else if (percentage >= 80) {
                status = "NEAR_LIMIT";
            } else {
                status = "UNDER_BUDGET";
            }
            
            utilization.add(BudgetUtilizationResponse.builder()
                    .categoryId(categoryId)
                    .categoryName(categoryName)
                    .budgetAmount(budgetAmount)
                    .spentAmount(spentAmount)
                    .remainingAmount(remaining)
                    .utilizationPercentage(percentage)
                    .status(status)
                    .build());
        }
        
        return utilization;
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}