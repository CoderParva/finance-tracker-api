package com.parva.financetracker.service;

import com.parva.financetracker.dto.CategoryRequest;
import com.parva.financetracker.dto.CategoryResponse;
import com.parva.financetracker.exception.ResourceNotFoundException;
import com.parva.financetracker.model.Category;
import com.parva.financetracker.model.User;
import com.parva.financetracker.repository.CategoryRepository;
import com.parva.financetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public CategoryResponse createCategory(String username, CategoryRequest request) {
        User user = getUserByUsername(username);
        
        Category category = Category.builder()
                .user(user)
                .name(request.getName())
                .type(request.getType())
                .color(request.getColor())
                .icon(request.getIcon())
                .build();
        
        category = categoryRepository.save(category);
        
        return mapToResponse(category);
    }
    
    public List<CategoryResponse> getAllCategories(String username) {
        User user = getUserByUsername(username);
        return categoryRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public CategoryResponse getCategoryById(String username, Long id) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        return mapToResponse(category);
    }
    
    public CategoryResponse updateCategory(String username, Long id, CategoryRequest request) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        category.setName(request.getName());
        category.setType(request.getType());
        category.setColor(request.getColor());
        category.setIcon(request.getIcon());
        
        category = categoryRepository.save(category);
        
        return mapToResponse(category);
    }
    
    public void deleteCategory(String username, Long id) {
        User user = getUserByUsername(username);
        Category category = categoryRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        
        categoryRepository.delete(category);
    }
    
    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
    
    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .type(category.getType())
                .color(category.getColor())
                .icon(category.getIcon())
                .build();
    }
}