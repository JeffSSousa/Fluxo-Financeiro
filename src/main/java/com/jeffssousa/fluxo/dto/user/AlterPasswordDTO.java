package com.jeffssousa.fluxo.dto.user;

import jakarta.validation.constraints.NotBlank;

public record AlterPasswordDTO(
        @NotBlank(message = "Digite a Senha!")
        String currentPassword,
        @NotBlank(message = "Digite a nova Senha!")
        String newPassword,
        @NotBlank(message = "Digite a confirmação da nova Senha!")
        String confirmPassword

    ) {
}
