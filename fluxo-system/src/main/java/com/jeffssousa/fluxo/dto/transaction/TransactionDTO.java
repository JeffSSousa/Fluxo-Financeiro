package com.jeffssousa.fluxo.dto.transaction;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDTO(

        @Schema(description = "Descrição da transação")
        String description,

        @Schema(description = "Tipo da transação, podendo ser receita ou despesa")
        String type,

        @Schema(description = "Valor da transação")
        BigDecimal amount,

        @Schema(description = "Data e hora em que a transação foi realizada")
        LocalDateTime transactionDate

) {
}