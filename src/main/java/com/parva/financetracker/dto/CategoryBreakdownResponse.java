package com.parva.financetracker.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryBreakdownResponse {
    private Long categoryId;
    private String categoryName;
    private String categoryIcon;
    private BigDecimal totalAmount;
    private Integer transactionCount;
    private Double percentageOfTotal;
}