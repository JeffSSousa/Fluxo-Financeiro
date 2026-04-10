package com.jeffssousa.fluxo.dto.income;

import com.jeffssousa.fluxo.enums.IncomeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record   IncomeRequestDTO(
        @NotBlank(message = "É obrigatorio conter uma descrição!")
        String description,
        @NotNull(message = "É obrigatorio conter o valor!")
        BigDecimal amount,
        @NotNull(message = "É obrigatorio conter uma data")
        LocalDateTime transactionDate,
        IncomeStatus status,
        String category
    ) {
}
