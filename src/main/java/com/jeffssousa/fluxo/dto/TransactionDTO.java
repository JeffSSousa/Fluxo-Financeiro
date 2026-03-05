package com.jeffssousa.fluxo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(
        String description,
        String type,
        BigDecimal amount,
        LocalDateTime transactionDate,
        boolean status
    ) {
}
