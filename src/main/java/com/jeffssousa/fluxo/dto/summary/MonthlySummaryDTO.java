package com.jeffssousa.fluxo.dto.summary;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record MonthlySummaryDTO(

        @Schema(description = "Mês de referência do resumo financeiro")
        String month,

        @Schema(description = "Total de receitas registradas no mês")
        BigDecimal income,

        @Schema(description = "Total de despesas registradas no mês")
        BigDecimal expense,

        @Schema(description = "Saldo do mês calculado pela diferença entre receitas e despesas")
        BigDecimal balance

) {
}