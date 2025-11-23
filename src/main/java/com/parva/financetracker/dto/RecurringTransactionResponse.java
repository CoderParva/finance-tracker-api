package com.parva.financetracker.dto;

import com.parva.financetracker.model.Frequency;
import com.parva.financetracker.model.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecurringTransactionResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private Frequency frequency;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextExecutionDate;
    private LocalDate lastExecutionDate;
    private Boolean isActive;
}