package com.parva.financetracker.controller;

import com.parva.financetracker.dto.BudgetRequest;
import com.parva.financetracker.dto.BudgetResponse;
import com.parva.financetracker.dto.MessageResponse;
import com.parva.financetracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin(origins = "*")
public class BudgetController {
    
    @Autowired
    private BudgetService budgetService;
    
    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @Valid @RequestBody BudgetRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        BudgetResponse response = budgetService.createBudget(username, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(Authentication authentication) {
        String username = authentication.getName();
        List<BudgetResponse> budgets = budgetService.getAllBudgets(username);
        return ResponseEntity.ok(budgets);
    }
    
    @GetMapping("/monthly")
    public ResponseEntity<List<BudgetResponse>> getBudgetsByMonthAndYear(
            @RequestParam Integer month,
            @RequestParam Integer year,
            Authentication authentication) {
        String username = authentication.getName();
        List<BudgetResponse> budgets = 
            budgetService.getBudgetsByMonthAndYear(username, month, year);
        return ResponseEntity.ok(budgets);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudgetById(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        BudgetResponse budget = budgetService.getBudgetById(username, id);
        return ResponseEntity.ok(budget);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        BudgetResponse response = budgetService.updateBudget(username, id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteBudget(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        budgetService.deleteBudget(username, id);
        return ResponseEntity.ok(new MessageResponse("Budget deleted successfully"));
    }
}