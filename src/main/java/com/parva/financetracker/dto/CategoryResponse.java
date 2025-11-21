package com.parva.financetracker.dto;

import com.parva.financetracker.model.TransactionType;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long id;
    private String name;
    private TransactionType type;
    private String color;
    private String icon;
}