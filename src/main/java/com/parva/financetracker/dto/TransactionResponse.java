package com.parva.financetracker.dto;

import com.parva.financetracker.model.TransactionType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDate transactionDate;
    private LocalDateTime createdAt;
}