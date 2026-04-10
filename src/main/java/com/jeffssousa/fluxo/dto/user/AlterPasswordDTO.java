package com.jeffssousa.fluxo.dto.user;

public record AlterPasswordDTO(

        String currentPassword,
        String newPassword,
        String confirmPassword

    ) {
}
