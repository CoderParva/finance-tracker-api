package com.parva.financetracker.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetRequest {
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Month is required")
    @Min(value = 1, message = "Month must be between 1 and 12")
    @Max(value = 12, message = "Month must be between 1 and 12")
    private Integer month;
    
    @NotNull(message = "Year is required")
    @Min(value = 2000, message = "Year must be 2000 or later")
    private Integer year;
}