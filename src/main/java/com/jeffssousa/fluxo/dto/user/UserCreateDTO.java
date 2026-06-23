package com.jeffssousa.fluxo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserCreateDTO(

        @Schema(
                description = "E-mail que será utilizado para acesso ao sistema",
                example = "jeff.sousa@email.com"
        )
        @Email(message = "Deve digitar um email valido!")
        @NotBlank(message = "Digite o e-mail!")
        String email,

        @Schema(
                description = "Senha de acesso do usuário",
                example = "Senha@123"
        )
        @NotBlank(message = "Digite a Senha!")
        String password,

        @Schema(
                description = "Nome do usuário",
                example = "Jefferson"
        )
        @NotBlank(message = "É obrigatorio conter o nome!")
        String name,

        @Schema(
                description = "Sobrenome do usuário",
                example = "Sousa"
        )
        @NotBlank(message = "É obrigatorio conter o sobrenome!")
        String lastName,

        @Schema(
                description = "Data de nascimento do usuário",
                example = "1995-08-15"
        )
        @NotNull(message = "É obrigatorio conter o data de nascimento!")
        LocalDate birthDate

) {
}
