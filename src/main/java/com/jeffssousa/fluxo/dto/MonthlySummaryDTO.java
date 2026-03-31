package com.jeffssousa.fluxo.dto;

import java.math.BigDecimal;

public record MonthlySummaryDTO(
        String month,
        BigDecimal income,
        BigDecimal expense,
        BigDecimal balancce
) {
}
