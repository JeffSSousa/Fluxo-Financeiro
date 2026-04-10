package com.jeffssousa.fluxo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserCreateDTO(
        @Email(message = "Deve digitar um email valido!")
        @NotBlank(message = "Digite o e-mail!")
        String email,
        @NotBlank(message = "Digite a Senha!")
        String password,

        @NotBlank(message = "É obrigatorio conter o nome!")
        String name,
        @NotBlank(message = "É obrigatorio conter o sobrenome!")
        String lastName,
        @NotNull(message = "É obrigatorio conter o data de nascimento!")
        LocalDate birthDate
    ) {
}
