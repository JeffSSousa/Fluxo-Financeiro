package com.jeffssousa.fluxo.dto.summary;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record AccountSummaryDTO(

        @Schema(description = "Valor total de receitas registradas")
        BigDecimal totalIncomes,

        @Schema(description = "Valor total de despesas registradas")
        BigDecimal totalExpense,

        @Schema(description = "Saldo atual calculado pela diferença entre receitas e despesas")
        BigDecimal balance

) {
}