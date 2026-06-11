package com.motiva.verdeinteligente.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record WeeklyPlanRequest(
    @NotNull
    LocalDate startDate,

    @NotNull
    @Min(1)
    @Max(10)
    Integer crewCount,

    @NotNull
    @Min(0)
    @Max(120)
    Double scenarioRainfallMm
) {
}
