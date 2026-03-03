package com.jeffssousa.fluxo.dto;

import java.time.LocalDate;

public record UserCreateDTO(
        String email,
        String password,

        String name,
        String lastName,
        LocalDate birthDate
    ) {
}
