package com.parva.financetracker.controller;

import com.parva.financetracker.dto.*;
import com.parva.financetracker.model.TransactionType;
import com.parva.financetracker.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {
    
    @Autowired
    private AnalyticsService analyticsService;
    
    @GetMapping("/summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @RequestParam Integer month,
            @RequestParam Integer year,
            Authentication authentication) {
        String username = authentication.getName();
        MonthlySummaryResponse summary = analyticsService.getMonthlySummary(username, month, year);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/category-breakdown")
    public ResponseEntity<List<CategoryBreakdownResponse>> getCategoryBreakdown(
            @RequestParam Integer month,
            @RequestParam Integer year,
            @RequestParam(defaultValue = "EXPENSE") TransactionType type,
            Authentication authentication) {
        String username = authentication.getName();
        List<CategoryBreakdownResponse> breakdown = analyticsService.getCategoryBreakdown(
                username, month, year, type);
        return ResponseEntity.ok(breakdown);
    }
    
    @GetMapping("/monthly-comparison")
    public ResponseEntity<List<MonthlyComparisonResponse>> getMonthlyComparison(
            @RequestParam(defaultValue = "6") Integer months,
            Authentication authentication) {
        String username = authentication.getName();
        List<MonthlyComparisonResponse> comparison = analyticsService.getMonthlyComparison(
                username, months);
        return ResponseEntity.ok(comparison);
    }
    
    @GetMapping("/budget-utilization")
    public ResponseEntity<List<BudgetUtilizationResponse>> getBudgetUtilization(
            @RequestParam Integer month,
            @RequestParam Integer year,
            Authentication authentication) {
        String username = authentication.getName();
        List<BudgetUtilizationResponse> utilization = analyticsService.getBudgetUtilization(
                username, month, year);
        return ResponseEntity.ok(utilization);
    }
}