package com.parva.financetracker.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetUtilizationResponse {
    private Long categoryId;
    private String categoryName;
    private BigDecimal budgetAmount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private Double utilizationPercentage;
    private String status; // "UNDER_BUDGET", "NEAR_LIMIT", "EXCEEDED"
}