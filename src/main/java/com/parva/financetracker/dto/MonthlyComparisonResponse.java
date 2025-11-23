package com.parva.financetracker.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyComparisonResponse {
    private Integer month;
    private Integer year;
    private String monthName;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private BigDecimal savings;
}