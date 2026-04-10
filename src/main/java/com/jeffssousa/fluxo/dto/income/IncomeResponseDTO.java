package com.jeffssousa.fluxo.dto.income;

import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.enums.IncomeStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncomeResponseDTO(
        UUID incomeId,
        String description,
        BigDecimal amount,
        LocalDateTime transactionDate,
        IncomeStatus status,
        CategoryResponseDTO category
) {
}
