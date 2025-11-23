package com.parva.financetracker.controller;

import com.parva.financetracker.dto.MessageResponse;
import com.parva.financetracker.dto.RecurringTransactionRequest;
import com.parva.financetracker.dto.RecurringTransactionResponse;
import com.parva.financetracker.service.RecurringTransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recurring-transactions")
@CrossOrigin(origins = "*")
public class RecurringTransactionController {
    
    @Autowired
    private RecurringTransactionService recurringTransactionService;
    
    @PostMapping
    public ResponseEntity<RecurringTransactionResponse> createRecurringTransaction(
            @Valid @RequestBody RecurringTransactionRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        RecurringTransactionResponse response = recurringTransactionService.createRecurringTransaction(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<List<RecurringTransactionResponse>> getAllRecurringTransactions(
            Authentication authentication) {
        String username = authentication.getName();
        List<RecurringTransactionResponse> transactions = recurringTransactionService.getAllRecurringTransactions(username);
        return ResponseEntity.ok(transactions);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RecurringTransactionResponse> getRecurringTransactionById(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        RecurringTransactionResponse response = recurringTransactionService.getRecurringTransactionById(username, id);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RecurringTransactionResponse> updateRecurringTransaction(
            @PathVariable Long id,
            @Valid @RequestBody RecurringTransactionRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        RecurringTransactionResponse response = recurringTransactionService.updateRecurringTransaction(username, id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteRecurringTransaction(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        recurringTransactionService.deleteRecurringTransaction(username, id);
        return ResponseEntity.ok(new MessageResponse("Recurring transaction deleted successfully"));
    }
    
    @PostMapping("/execute")
    public ResponseEntity<MessageResponse> executeDueRecurringTransactions(
            Authentication authentication) {
        String username = authentication.getName();
        int executedCount = recurringTransactionService.executeDueRecurringTransactions(username);
        return ResponseEntity.ok(new MessageResponse(executedCount + " recurring transactions executed successfully"));
    }
}