package com.jeffssousa.fluxo.dto.expense;

import com.jeffssousa.fluxo.enums.ExpenseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ExpenseRequestDTO(

        @Schema(
                description = "Descrição da despesa",
                example = "Conta de energia elétrica"
        )
        @NotBlank(message = "É obrigatorio conter uma descrição!")
        String description,

        @Schema(
                description = "Valor da despesa",
                example = "189.90"
        )
        @NotNull(message = "É obrigatorio conter o valor!")
        BigDecimal amount,

        @Schema(
                description = "Data e hora em que a despesa foi realizada",
                example = "2026-06-23T14:30:00"
        )
        @NotNull(message = "É obrigatorio conter uma data")
        LocalDateTime transactionDate,

        @Schema(
                description = "Data de vencimento da despesa",
                example = "2026-07-10"
        )
        LocalDate dueDate,

        @Schema(
                description = "Status atual da despesa",
                example = "PENDING"
        )
        ExpenseStatus status,

        @Schema(
                description = "Categoria da despesa",
                example = "Moradia"
        )
        String category

) {
}
