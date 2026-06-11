package com.motiva.verdeinteligente.dto;

import java.time.LocalDate;
import java.util.List;

public record WeeklyPlanResponse(
    Long id,
    LocalDate startDate,
    int requestedCrewCount,
    double scenarioRainfallMm,
    int totalAssignedHours,
    int pendingSegments,
    List<WeeklyPlanItemResponse> items
) {
}
