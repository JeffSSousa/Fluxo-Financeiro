package com.jeffssousa.fluxo.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDTO(

        @Schema(description = "Token JWT utilizado para autenticação nas requisições")
        String token,

        @Schema(description = "Tipo do token de autenticação")
        String type

) {
}