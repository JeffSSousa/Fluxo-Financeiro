package com.jeffssousa.fluxo.dto;

import java.time.LocalDate;

public record ProfileResponseDTO(
    String name,
    String lastName,
    LocalDate birthDate
    ) {
}
