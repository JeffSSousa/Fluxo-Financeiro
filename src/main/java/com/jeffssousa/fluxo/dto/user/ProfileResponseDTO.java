package com.jeffssousa.fluxo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Informações básicas do perfil do usuário")
public record ProfileResponseDTO(

        @Schema(description = "Nome do usuário")
        String name,

        @Schema(description = "Sobrenome do usuário")
        String lastName,

        @Schema(description = "Data de nascimento do usuário")
        LocalDate birthDate

) {
}