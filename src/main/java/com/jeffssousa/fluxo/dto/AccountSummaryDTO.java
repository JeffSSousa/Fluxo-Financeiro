package com.jeffssousa.fluxo.dto;

import java.math.BigDecimal;

public record AccountSummaryDTO(
        BigDecimal totalIncomes,
        BigDecimal totalExpense,
        BigDecimal balance
    ) {
}
