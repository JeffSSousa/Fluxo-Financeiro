package com.jeffssousa.fluxo.dto.category;

import com.jeffssousa.fluxo.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
        @NotBlank
        @Size(max = 50, message = "O nome deve conter no maximo 50 caracteres")
        String name,
        @NotNull(message = "Toda categoria deve conter um tipo (INCOME ou EXPENSE)")
        CategoryType type
    ) {
}
