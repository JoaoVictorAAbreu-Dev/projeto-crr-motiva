package com.motiva.verdeinteligente.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RecalculatePrioritiesRequest(
    @NotNull
    @Min(0)
    @Max(120)
    Double rainfallDeltaMm,

    @NotNull
    @Min(0)
    @Max(5)
    Integer inspectorSignalBoost
) {
}
