package com.parva.financetracker.service;

import com.parva.financetracker.dto.BudgetRequest;
import com.parva.financetracker.dto.BudgetResponse;
import com.parva.financetracker.exception.ResourceAlreadyExistsException;
import com.parva.financetracker.exception.ResourceNotFoundException;
import com.parva.financetracker.model.Budget;
import com.parva.financetracker.model.Category;
import com.parva.financetracker.model.TransactionType;
import com.parva.financetracker.model.User;
import com.parva.financetracker.repository.BudgetRepository;
import com.parva.financetracker.repository.CategoryRepository;
import com.parva.financetracker.repository.TransactionRepository;
import com.parva.financetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetService {
    
    @Autowired
    private BudgetRepository budgetRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    public BudgetResponse createBudget(String username, BudgetRequest request) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        if (budgetRepository.existsByUserIdAndCategoryIdAndMonthAndYear(
                user.getId(), request.getCategoryId(), request.getMonth(), request.getYear())) {
            throw new ResourceAlreadyExistsException(
                "Budget already exists for this category in the specified month and year");
        }
        
        Budget budget = Budget.builder()
                .user(user)
                .category(category)
                .amount(request.getAmount())
                .month(request.getMonth())
                .year(request.getYear())
                .build();
        
        budget = budgetRepository.save(budget);
        
        return mapToResponse(budget, user.getId());
    }
    
    public List<BudgetResponse> getAllBudgets(String username) {
        User user = getUserByUsername(username);
        return budgetRepository.findByUserId(user.getId())
                .stream()
                .map(budget -> mapToResponse(budget, user.getId()))
                .collect(Collectors.toList());
    }
    
    public List<BudgetResponse> getBudgetsByMonthAndYear(
            String username, Integer month, Integer year) {
        User user = getUserByUsername(username);
        return budgetRepository.findByUserIdAndMonthAndYear(user.getId(), month, year)
                .stream()
                .map(budget -> mapToResponse(budget, user.getId()))
                .collect(Collectors.toList());
    }
    
    public BudgetResponse getBudgetById(String username, Long id) {
        User user = getUserByUsername(username);
        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        
        return mapToResponse(budget, user.getId());
    }
    
    public BudgetResponse updateBudget(String username, Long id, BudgetRequest request) {
        User user = getUserByUsername(username);
        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        budget.setCategory(category);
        budget.setAmount(request.getAmount());
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        
        budget = budgetRepository.save(budget);
        
        return mapToResponse(budget, user.getId());
    }
    
    public void deleteBudget(String username, Long id) {
        User user = getUserByUsername(username);
        Budget budget = budgetRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found"));
        
        budgetRepository.delete(budget);
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    private BudgetResponse mapToResponse(Budget budget, Long userId) {
        BigDecimal spent = transactionRepository.sumAmountByUserIdAndCategoryIdAndTypeAndMonthAndYear(
                userId,
                budget.getCategory().getId(),
                TransactionType.EXPENSE,
                budget.getMonth(),
                budget.getYear()
        );
        
        if (spent == null) {
            spent = BigDecimal.ZERO;
        }
        
        BigDecimal remaining = budget.getAmount().subtract(spent);
        
        return BudgetResponse.builder()
                .id(budget.getId())
                .categoryId(budget.getCategory().getId())
                .categoryName(budget.getCategory().getName())
                .amount(budget.getAmount())
                .spent(spent)
                .remaining(remaining)
                .month(budget.getMonth())
                .year(budget.getYear())
                .build();
    }
}
