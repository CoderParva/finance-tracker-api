package com.parva.financetracker.service;

import com.parva.financetracker.dto.RecurringTransactionRequest;
import com.parva.financetracker.dto.RecurringTransactionResponse;
import com.parva.financetracker.exception.ResourceNotFoundException;
import com.parva.financetracker.model.*;
import com.parva.financetracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecurringTransactionService {
    
    @Autowired
    private RecurringTransactionRepository recurringTransactionRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public RecurringTransactionResponse createRecurringTransaction(
            String username, RecurringTransactionRequest request) {
        User user = getUserByUsername(username);
        
        Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        RecurringTransaction recurring = RecurringTransaction.builder()
                .user(user)
                .category(category)
                .amount(request.getAmount())
                .type(request.getType())
                .description(request.getDescription())
                .frequency(request.getFrequency())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .nextExecutionDate(request.getNextExecutionDate())
                .isActive(request.getIsActive())
                .build();
        
        recurring = recurringTransactionRepository.save(recurring);
        return mapToResponse(recurring);
    }
    
    public List<RecurringTransactionResponse> getAllRecurringTransactions(String username) {
        User user = getUserByUsername(username);
        List<RecurringTransaction> transactions = recurringTransactionRepository.findByUserId(user.getId());
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public RecurringTransactionResponse getRecurringTransactionById(String username, Long id) {
        User user = getUserByUsername(username);
        RecurringTransaction recurring = recurringTransactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recurring transaction not found"));
        return mapToResponse(recurring);
    }
    
    public RecurringTransactionResponse updateRecurringTransaction(
            String username, Long id, RecurringTransactionRequest request) {
        User user = getUserByUsername(username);
        
        RecurringTransaction recurring = recurringTransactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recurring transaction not found"));
        
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findByIdAndUserId(request.getCategoryId(), user.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            recurring.setCategory(category);
        }
        
        if (request.getAmount() != null) recurring.setAmount(request.getAmount());
        if (request.getType() != null) recurring.setType(request.getType());
        if (request.getDescription() != null) recurring.setDescription(request.getDescription());
        if (request.getFrequency() != null) recurring.setFrequency(request.getFrequency());
        if (request.getStartDate() != null) recurring.setStartDate(request.getStartDate());
        if (request.getEndDate() != null) recurring.setEndDate(request.getEndDate());
        if (request.getNextExecutionDate() != null) recurring.setNextExecutionDate(request.getNextExecutionDate());
        if (request.getIsActive() != null) recurring.setIsActive(request.getIsActive());
        
        recurring = recurringTransactionRepository.save(recurring);
        return mapToResponse(recurring);
    }
    
    public void deleteRecurringTransaction(String username, Long id) {
        User user = getUserByUsername(username);
        RecurringTransaction recurring = recurringTransactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recurring transaction not found"));
        recurringTransactionRepository.delete(recurring);
    }
    
    public int executeDueRecurringTransactions(String username) {
        User user = getUserByUsername(username);
        LocalDate today = LocalDate.now();
        
        List<RecurringTransaction> dueTransactions = recurringTransactionRepository
                .findByUserIdAndIsActiveAndNextExecutionDateLessThanEqual(user.getId(), true, today);
        
        int executedCount = 0;
        
        for (RecurringTransaction recurring : dueTransactions) {
            // Create the transaction
            Transaction transaction = Transaction.builder()
                    .user(user)
                    .category(recurring.getCategory())
                    .amount(recurring.getAmount())
                    .type(recurring.getType())
                    .description(recurring.getDescription() + " (Auto-generated)")
                    .transactionDate(today)
                    .build();
            
            transactionRepository.save(transaction);
            
            // Update recurring transaction
            recurring.setLastExecutionDate(today);
            recurring.setNextExecutionDate(calculateNextExecutionDate(today, recurring.getFrequency()));
            
            // Check if we've passed the end date
            if (recurring.getEndDate() != null && recurring.getNextExecutionDate().isAfter(recurring.getEndDate())) {
                recurring.setIsActive(false);
            }
            
            recurringTransactionRepository.save(recurring);
            executedCount++;
        }
        
        return executedCount;
    }
    
    private LocalDate calculateNextExecutionDate(LocalDate currentDate, Frequency frequency) {
        switch (frequency) {
            case DAILY:
                return currentDate.plusDays(1);
            case WEEKLY:
                return currentDate.plusWeeks(1);
            case MONTHLY:
                return currentDate.plusMonths(1);
            case YEARLY:
                return currentDate.plusYears(1);
            default:
                return currentDate.plusMonths(1);
        }
    }
    
    private RecurringTransactionResponse mapToResponse(RecurringTransaction recurring) {
        return RecurringTransactionResponse.builder()
                .id(recurring.getId())
                .categoryId(recurring.getCategory().getId())
                .categoryName(recurring.getCategory().getName())
                .amount(recurring.getAmount())
                .type(recurring.getType())
                .description(recurring.getDescription())
                .frequency(recurring.getFrequency())
                .startDate(recurring.getStartDate())
                .endDate(recurring.getEndDate())
                .nextExecutionDate(recurring.getNextExecutionDate())
                .lastExecutionDate(recurring.getLastExecutionDate())
                .isActive(recurring.getIsActive())
                .build();
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}