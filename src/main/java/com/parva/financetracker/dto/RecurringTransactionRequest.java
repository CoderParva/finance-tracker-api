package com.parva.financetracker.dto;

import com.parva.financetracker.model.Frequency;
import com.parva.financetracker.model.TransactionType;
import lombok.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringTransactionRequest {
    
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotNull(message = "Type is required")
    private TransactionType type;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Frequency is required")
    private Frequency frequency;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    private LocalDate endDate;
    
    @NotNull(message = "Next execution date is required")
    private LocalDate nextExecutionDate;
    
    @Builder.Default
    private Boolean isActive = true;
}