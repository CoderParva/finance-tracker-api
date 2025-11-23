package com.parva.financetracker.repository;

import com.parva.financetracker.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    
    List<Budget> findByUserId(Long userId);
    
    Optional<Budget> findByIdAndUserId(Long id, Long userId);
    
    List<Budget> findByUserIdAndMonthAndYear(Long userId, Integer month, Integer year);
    
    boolean existsByUserIdAndCategoryIdAndMonthAndYear(
            Long userId, Long categoryId, Integer month, Integer year
    );
    
    @Query("SELECT SUM(b.amount) FROM Budget b WHERE b.user.id = :userId AND b.month = :month AND b.year = :year")
    BigDecimal sumBudgetAmountByUserIdAndMonthAndYear(
            @Param("userId") Long userId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );
    
    @Query("SELECT b.category.id, b.category.name, b.amount, " +
           "(SELECT SUM(t.amount) FROM Transaction t " +
           "WHERE t.category.id = b.category.id AND t.user.id = :userId " +
           "AND t.type = 'EXPENSE' AND MONTH(t.transactionDate) = :month " +
           "AND YEAR(t.transactionDate) = :year) " +
           "FROM Budget b WHERE b.user.id = :userId AND b.month = :month AND b.year = :year")
    List<Object[]> getBudgetUtilization(
            @Param("userId") Long userId,
            @Param("month") Integer month,
            @Param("year") Integer year
    );
}