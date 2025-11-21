package com.parva.financetracker.controller;

import com.parva.financetracker.dto.CategoryRequest;
import com.parva.financetracker.dto.CategoryResponse;
import com.parva.financetracker.dto.MessageResponse;
import com.parva.financetracker.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        CategoryResponse response = categoryService.createCategory(username, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(Authentication authentication) {
        String username = authentication.getName();
        List<CategoryResponse> categories = categoryService.getAllCategories(username);
        return ResponseEntity.ok(categories);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        CategoryResponse category = categoryService.getCategoryById(username, id);
        return ResponseEntity.ok(category);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        CategoryResponse response = categoryService.updateCategory(username, id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deleteCategory(
            @PathVariable Long id,
            Authentication authentication) {
        String username = authentication.getName();
        categoryService.deleteCategory(username, id);
        return ResponseEntity.ok(new MessageResponse("Category deleted successfully"));
    }
}
