package com.parva.financetracker.repository;

import com.parva.financetracker.model.Transaction;
import com.parva.financetracker.model.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    List<Transaction> findByUserId(Long userId);
    
    List<Transaction> findByUserIdAndType(Long userId, TransactionType type);
    
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);
    
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
           "AND t.transactionDate BETWEEN :startDate AND :endDate " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserIdAndDateRange(
        @Param("userId") Long userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
    
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
           "AND MONTH(t.transactionDate) = :month " +
           "AND YEAR(t.transactionDate) = :year " +
           "ORDER BY t.transactionDate DESC")
    List<Transaction> findByUserIdAndMonthAndYear(
        @Param("userId") Long userId,
        @Param("month") Integer month,
        @Param("year") Integer year
    );
    
    @Query("SELECT t FROM Transaction t WHERE t.user.id = :userId " +
           "AND t.category.id = :categoryId " +
           "AND MONTH(t.transactionDate) = :month " +
           "AND YEAR(t.transactionDate) = :year")
    List<Transaction> findByUserIdAndCategoryIdAndMonthAndYear(
        @Param("userId") Long userId,
        @Param("categoryId") Long categoryId,
        @Param("month") Integer month,
        @Param("year") Integer year
    );
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user.id = :userId " +
           "AND t.category.id = :categoryId " +
           "AND t.type = :type " +
           "AND MONTH(t.transactionDate) = :month " +
           "AND YEAR(t.transactionDate) = :year")
    BigDecimal sumAmountByUserIdAndCategoryIdAndTypeAndMonthAndYear(
        @Param("userId") Long userId,
        @Param("categoryId") Long categoryId,
        @Param("type") TransactionType type,
        @Param("month") Integer month,
        @Param("year") Integer year
    );
    
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t " +
           "WHERE t.user.id = :userId " +
           "AND t.type = :type " +
           "AND MONTH(t.transactionDate) = :month " +
           "AND YEAR(t.transactionDate) = :year")
    BigDecimal sumAmountByUserIdAndTypeAndMonthAndYear(
        @Param("userId") Long userId,
        @Param("type") TransactionType type,
        @Param("month") Integer month,
        @Param("year") Integer year
    );
}