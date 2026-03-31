package com.jeffssousa.fluxo.repository;

import com.jeffssousa.fluxo.entities.Expense;
import com.jeffssousa.fluxo.entities.User;
import com.jeffssousa.fluxo.repository.projection.MonthlyAmountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    @Query("""
       SELECT COALESCE(SUM(i.amount), 0)
       FROM Expense i
       WHERE i.user = :user
       """)
    BigDecimal sumByUser(@Param("user") User user);

    List<Expense> findAllByUser(User user);

    List<Expense> findTop15ByUserAndDueDateBetweenOrderByDueDateAsc(
            User user,
            LocalDate start,
            LocalDate end
    );

    @Query(value = """
    SELECT 
        MONTH(e.transaction_date) AS mes,
        COALESCE(SUM(e.amount), 0) AS total
    FROM tb_expenses e
    WHERE e.user_id = :userId
      AND YEAR(e.transaction_date) = :year
    GROUP BY MONTH(e.transaction_date)
    ORDER BY mes
    """, nativeQuery = true)
    List<MonthlyAmountProjection> findMonthlyExpenses(UUID userId, Integer year);
}
