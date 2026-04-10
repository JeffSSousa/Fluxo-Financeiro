package com.jeffssousa.fluxo.dto.summary;

import java.util.List;

public record YearlySummaryDTO(
        Integer year,
        List<MonthlySummaryDTO> months
) {
}
