package com.parva.financetracker.repository;

import com.parva.financetracker.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
    List<Budget> findByUserId(Long userId);
    
    Optional<Budget> findByIdAndUserId(Long id, Long userId);
    
    Optional<Budget> findByUserIdAndCategoryIdAndMonthAndYear(
        Long userId, 
        Long categoryId, 
        Integer month, 
        Integer year
    );
    
    List<Budget> findByUserIdAndMonthAndYear(
        Long userId, 
        Integer month, 
        Integer year
    );
    
    Boolean existsByUserIdAndCategoryIdAndMonthAndYear(
        Long userId,
        Long categoryId,
        Integer month,
        Integer year
    );
}