package com.jeffssousa.fluxo.dto;

import com.jeffssousa.fluxo.entities.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncomeResponseDTO(
        UUID incomeId,
        String description,
        BigDecimal amount,
        LocalDateTime transactionDate,
        Boolean status
) {
}
