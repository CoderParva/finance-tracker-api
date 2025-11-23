package com.parva.financetracker.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlySummaryResponse {
    private Integer month;
    private Integer year;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal savings;
    private BigDecimal budgetAllocated;
    private BigDecimal budgetUsed;
    private Double budgetUtilizationPercentage;
}