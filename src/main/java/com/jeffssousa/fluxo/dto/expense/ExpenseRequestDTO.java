package com.jeffssousa.fluxo.dto.expense;

import com.jeffssousa.fluxo.enums.ExpenseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpenseRequestDTO(
        @NotBlank(message = "É obrigatorio conter uma descrição!")
        String description,
        @NotNull(message = "É obrigatorio conter o valor!")
        BigDecimal amount,
        @NotNull(message = "É obrigatorio conter uma data")
        LocalDateTime transactionDate,
        LocalDate dueDate,
        ExpenseStatus status,
        String category
    ) {
}
