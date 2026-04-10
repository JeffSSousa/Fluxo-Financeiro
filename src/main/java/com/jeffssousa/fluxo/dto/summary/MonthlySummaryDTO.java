package com.jeffssousa.fluxo.dto.summary;

import java.math.BigDecimal;

public record MonthlySummaryDTO(
        String month,
        BigDecimal income,
        BigDecimal expense,
        BigDecimal balancce
) {
}
