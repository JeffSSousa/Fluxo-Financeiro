package com.jeffssousa.fluxo.dto.expense;

import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.enums.ExpenseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponseDTO (
    UUID expenseId,
    String description,
    BigDecimal amount,
    LocalDateTime transactionDate,
    LocalDate dueDate,
    ExpenseStatus status,
    CategoryResponseDTO category
    ){
}
