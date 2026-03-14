package com.jeffssousa.fluxo.dto;

import com.jeffssousa.fluxo.enums.IncomeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record   IncomeRequestDTO(
        String description,
        BigDecimal amount,
        LocalDateTime transactionDate,
        IncomeStatus status,
        String category
    ) {
}
