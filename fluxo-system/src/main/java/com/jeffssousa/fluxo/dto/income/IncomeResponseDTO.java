package com.jeffssousa.fluxo.dto.income;

import com.jeffssousa.fluxo.dto.category.CategoryResponseDTO;
import com.jeffssousa.fluxo.enums.IncomeStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncomeResponseDTO(

        @Schema(description = "Identificador único da receita")
        UUID incomeId,

        @Schema(description = "Descrição da receita")
        String description,

        @Schema(description = "Valor da receita")
        BigDecimal amount,

        @Schema(description = "Data e hora em que a receita foi registrada")
        LocalDateTime transactionDate,

        @Schema(description = "Status atual da receita")
        IncomeStatus status,

        @Schema(description = "Categoria associada à receita")
        CategoryResponseDTO category

) {
}