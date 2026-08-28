package com.jeffssousa.fluxo.dto.summary;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record YearlySummaryDTO(

        @Schema(description = "Ano de referência do resumo financeiro")
        Integer year,

        @Schema(description = "Lista contendo os resumos financeiros de cada mês do ano")
        List<MonthlySummaryDTO> months

) {
}