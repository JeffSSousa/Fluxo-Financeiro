package com.jeffssousa.fluxo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponseDTO (
    UUID expenseId,
    String description,
    BigDecimal amount,
    LocalDateTime transactionDate,
    boolean status
    ){
}
