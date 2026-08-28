package com.jeffssousa.fluxo.dto.category;

import com.jeffssousa.fluxo.enums.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryResponseDTO(
        @Schema(description = "ID da Categoria")
        Long categoryId,
        @Schema(description = "Nome da categoria")
        String name,
        @Schema(description = "Tipo da categoria")
        CategoryType type
    ) {
}
