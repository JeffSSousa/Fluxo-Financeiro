package com.jeffssousa.fluxo.dto.income;

import com.jeffssousa.fluxo.enums.IncomeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record IncomeRequestDTO(

        @Schema(
                description = "Descrição da receita",
                example = "Salário mensal"
        )
        @NotBlank(message = "É obrigatorio conter uma descrição!")
        @Size(
                max = 255,
                message = "A descrição deve conter no máximo 255 caracteres"
        )
        String description,

        @Schema(
                description = "Valor da receita",
                example = "5500.00"
        )
        @NotNull(message = "É obrigatorio conter o valor!")
        @DecimalMin(
                value = "0.01",
                message = "O valor deve ser maior que zero"
        )
        BigDecimal amount,

        @Schema(
                description = "Data e hora em que a receita foi realizada",
                example = "2026-06-23T14:30:00"
        )
        @NotNull(message = "É obrigatorio conter uma data")
        LocalDateTime transactionDate,

        @Schema(
                description = "Status atual da receita",
                example = "RECEIVED"
        )
        IncomeStatus status,

        @Schema(
                description = "Categoria da receita",
                example = "Salário"
        )
        String category

) {
}