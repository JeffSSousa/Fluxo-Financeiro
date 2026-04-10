package com.jeffssousa.fluxo.dto.summary;

import java.math.BigDecimal;

public record AccountSummaryDTO(
        BigDecimal totalIncomes,
        BigDecimal totalExpense,
        BigDecimal balance
    ) {
}
