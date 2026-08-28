package com.jeffssousa.fluxo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AlterPasswordDTO(

        @Schema(
                description = "Senha atual do usuário",
                example = "SenhaAtual@123"
        )
        @NotBlank(message = "Digite a Senha!")
        String currentPassword,

        @Schema(
                description = "Nova senha que será utilizada pelo usuário",
                example = "NovaSenha@123"
        )
        @NotBlank(message = "Digite a nova Senha!")
        String newPassword,

        @Schema(
                description = "Confirmação da nova senha. Deve ser igual à nova senha informada",
                example = "NovaSenha@123"
        )
        @NotBlank(message = "Digite a confirmação da nova Senha!")
        String confirmPassword

) {
}
