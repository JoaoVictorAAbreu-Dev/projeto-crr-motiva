package com.motiva.verdeinteligente.dto;

import java.time.LocalDate;

public record MaintenanceEventResponse(
    Long id,
    String segmentName,
    LocalDate eventDate,
    String type,
    double costEstimate,
    String crewName
) {
}
