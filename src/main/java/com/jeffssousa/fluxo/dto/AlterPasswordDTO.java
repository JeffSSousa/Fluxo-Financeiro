package com.jeffssousa.fluxo.dto;

public record AlterPasswordDTO(

        String currentPassword,
        String newPassword,
        String confirmPassword

    ) {
}
