package com.motiva.verdeinteligente.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

public record MaintenanceEventRequest(
    @NotNull
    UUID segmentId,

    @NotNull
    LocalDate eventDate,

    @NotBlank
    String type,

    @Min(0)
    double costEstimate,

    @NotBlank
    String crewName,

    String notes
) {
}
