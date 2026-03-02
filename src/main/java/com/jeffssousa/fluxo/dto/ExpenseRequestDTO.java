package com.jeffssousa.fluxo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseRequestDTO(
        String description,
        BigDecimal amount,
        LocalDateTime transactionDate,
        boolean status
    ) {
}
