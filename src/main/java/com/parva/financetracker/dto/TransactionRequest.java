package com.parva.financetracker.dto;

import com.parva.financetracker.model.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Transaction type is required")
    private TransactionType type;
    
    private String description;
    
    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;
}