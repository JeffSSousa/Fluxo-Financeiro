package com.jeffssousa.fluxo.dto;

import com.jeffssousa.fluxo.enums.ExpenseStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpenseRequestDTO(
        String description,
        BigDecimal amount,
        LocalDateTime transactionDate,
        LocalDate dueDate,
        ExpenseStatus status,
        String category
    ) {
}
