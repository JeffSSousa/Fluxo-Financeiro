package com.jeffssousa.fluxo.dto.category;

import com.jeffssousa.fluxo.enums.CategoryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
        @Schema(description = "Nome da categoria", example = "Mercado")
        @NotBlank
        @Size(max = 50, message = "O nome deve conter no maximo 50 caracteres")
        String name,
        @Schema(description = "Tipo da categoria", example = "INCOME ou EXPENSE")
        @NotNull(message = "Toda categoria deve conter um tipo (INCOME ou EXPENSE)")
        CategoryType type
    ) {
}
