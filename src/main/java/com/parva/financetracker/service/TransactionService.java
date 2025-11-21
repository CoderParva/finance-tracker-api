package com.parva.financetracker.service;

import com.parva.financetracker.dto.TransactionRequest;
import com.parva.financetracker.dto.TransactionResponse;
import com.parva.financetracker.exception.ResourceNotFoundException;
import com.parva.financetracker.model.Category;
import com.parva.financetracker.model.Transaction;
import com.parva.financetracker.model.User;
import com.parva.financetracker.repository.CategoryRepository;
import com.parva.financetracker.repository.TransactionRepository;
import com.parva.financetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    
    @Autowired
    private TransactionRepository transactionRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public TransactionResponse createTransaction(String username, TransactionRequest request) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        Transaction transaction = Transaction.builder()
                .user(user)
                .category(category)
                .amount(request.getAmount())
                .type(request.getType())
                .description(request.getDescription())
                .transactionDate(request.getTransactionDate())
                .build();
        
        transaction = transactionRepository.save(transaction);
        
        return mapToResponse(transaction);
    }
    
    public List<TransactionResponse> getAllTransactions(String username) {
        User user = getUserByUsername(username);
        return transactionRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public TransactionResponse getTransactionById(String username, Long id) {
        User user = getUserByUsername(username);
        Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        
        return mapToResponse(transaction);
    }
    
    public TransactionResponse updateTransaction(String username, Long id, TransactionRequest request) {
        User user = getUserByUsername(username);
        Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        transaction.setCategory(category);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setTransactionDate(request.getTransactionDate());
        
        transaction = transactionRepository.save(transaction);
        
        return mapToResponse(transaction);
    }
    
    public void deleteTransaction(String username, Long id) {
        User user = getUserByUsername(username);
        Transaction transaction = transactionRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        
        transactionRepository.delete(transaction);
    }
    
    public List<TransactionResponse> getTransactionsByDateRange(
            String username, LocalDate startDate, LocalDate endDate) {
        User user = getUserByUsername(username);
        return transactionRepository.findByUserIdAndDateRange(user.getId(), startDate, endDate)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public List<TransactionResponse> getTransactionsByMonth(
            String username, Integer month, Integer year) {
        User user = getUserByUsername(username);
        return transactionRepository.findByUserIdAndMonthAndYear(user.getId(), month, year)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .categoryId(transaction.getCategory().getId())
                .categoryName(transaction.getCategory().getName())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .transactionDate(transaction.getTransactionDate())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
