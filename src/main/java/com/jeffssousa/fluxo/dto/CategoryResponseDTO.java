package com.jeffssousa.fluxo.dto;

import com.jeffssousa.fluxo.enums.CategoryType;

public record CategoryResponseDTO(
        Long categoryId,
        String name,
        CategoryType type
    ) {
}
