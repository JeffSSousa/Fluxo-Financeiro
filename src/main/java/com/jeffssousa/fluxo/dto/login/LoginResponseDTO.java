package com.jeffssousa.fluxo.dto.login;

public record LoginResponseDTO(
        String token,
        String type
    ) {
}
