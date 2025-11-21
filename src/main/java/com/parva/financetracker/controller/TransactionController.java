package com.parva.financetracker.controller;

import com.parva.financetracker.dto.MessageResponse;
import com.parva.financetracker.dto.TransactionRequest;
import com.parva.financetracker.dto.TransactionResponse;
import com.parva.financetracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;
    
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        TransactionResponse response = transactionService.createTransaction(username, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            Authentication authentication) {
        String username = authentication.getName();
        List<TransactionResponse> transactions = transactionService.getAllTransactions(username);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        TransactionResponse transaction = transactionService.getTransactionById(username, id);
        return ResponseEntity.ok(transaction);
    }
    
    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        String username = authentication.getName();
        List<TransactionResponse> transactions = 
            transactionService.getTransactionsByDateRange(username, startDate, endDate);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/monthly")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByMonth(
            @RequestParam Integer month,
            @RequestParam Integer year,
            Authentication authentication) {
        String username = authentication.getName();
        List<TransactionResponse> transactions = 
            transactionService.getTransactionsByMonth(username, month, year);
        return ResponseEntity.ok(transactions);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        TransactionResponse response = transactionService.updateTransaction(username, id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteTransaction(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        transactionService.deleteTransaction(username, id);
        return ResponseEntity.ok(new MessageResponse("Transaction deleted successfully"));
    }
}
