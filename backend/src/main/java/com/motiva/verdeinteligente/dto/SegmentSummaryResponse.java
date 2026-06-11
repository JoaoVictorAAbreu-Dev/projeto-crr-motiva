package com.motiva.verdeinteligente.dto;

import com.motiva.verdeinteligente.model.OperationalCriticality;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.model.VegetationClass;
import java.time.LocalDate;
import java.util.UUID;

public record SegmentSummaryResponse(
    UUID id,
    String highway,
    String name,
    double kmStart,
    double kmEnd,
    double extensionKm,
    VegetationClass vegetationClass,
    OperationalCriticality operationalCriticality,
    LocalDate lastMowingDate,
    int historicalFrequencyDays,
    boolean sensitiveArea,
    boolean contractualPressure,
    double mapX,
    double mapY,
    double latitude,
    double longitude,
    double score,
    PriorityLevel priorityLevel,
    String reasons
) {
}
