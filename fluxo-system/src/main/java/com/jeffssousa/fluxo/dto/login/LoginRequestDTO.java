package com.jeffssousa.fluxo.dto.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(

        @Schema(
                description = "E-mail do usuário cadastrado",
                example = "jeff.sousa@email.com"
        )
        @Email
        @NotBlank
        String email,

        @Schema(
                description = "Senha de acesso do usuário",
                example = "Senha@123"
        )
        @NotBlank
        String password

) {
}