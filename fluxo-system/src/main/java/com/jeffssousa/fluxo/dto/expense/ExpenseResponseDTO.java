package com.jeffssousa.fluxo.dto.expense;

import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.enums.ExpenseStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponseDTO(

        @Schema(description = "Identificador único da despesa")
        UUID expenseId,

        @Schema(description = "Descrição da despesa")
        String description,

        @Schema(description = "Valor da despesa")
        BigDecimal amount,

        @Schema(description = "Data e hora em que a despesa foi registrada")
        LocalDateTime transactionDate,

        @Schema(description = "Data de vencimento da despesa")
        LocalDate dueDate,

        @Schema(description = "Status atual da despesa")
        ExpenseStatus status,

        @Schema(description = "Categoria associada à despesa")
        CategoryResponseDTO category

) {
}