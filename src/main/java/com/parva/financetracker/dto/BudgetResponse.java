package com.parva.financetracker.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
    private BigDecimal spent;
    private BigDecimal remaining;
    private Integer month;
    private Integer year;
}