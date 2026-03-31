package com.jeffssousa.fluxo.dto;

import java.util.List;

public record YearlySummaryDTO(
        Integer year,
        List<MonthlySummaryDTO> months
) {
}
