package com.parva.financetracker.repository;

import com.parva.financetracker.model.RecurringTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, Long> {
    
    List<RecurringTransaction> findByUserId(Long userId);
    
    Optional<RecurringTransaction> findByIdAndUserId(Long id, Long userId);
    
    List<RecurringTransaction> findByUserIdAndIsActive(Long userId, Boolean isActive);
    
    List<RecurringTransaction> findByUserIdAndIsActiveAndNextExecutionDateLessThanEqual(
            Long userId, Boolean isActive, LocalDate date);
}