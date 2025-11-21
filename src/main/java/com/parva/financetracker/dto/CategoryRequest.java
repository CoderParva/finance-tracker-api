package com.parva.financetracker.dto;

import com.parva.financetracker.model.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequest {
    
    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must not exceed 50 characters")
    private String name;
    
    @NotNull(message = "Category type is required")
    private TransactionType type;
    
    private String color;
    private String icon;
}