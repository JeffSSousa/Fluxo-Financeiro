package com.jeffssousa.fluxo.dto.category;

import com.jeffssousa.fluxo.enums.CategoryType;

public record CategoryRequestDTO(
        String name,
        CategoryType type
    ) {
}
