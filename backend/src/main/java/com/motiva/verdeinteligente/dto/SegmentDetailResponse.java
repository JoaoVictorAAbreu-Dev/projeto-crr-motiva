package com.motiva.verdeinteligente.dto;

import com.motiva.verdeinteligente.model.OperationalCriticality;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.model.VegetationClass;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SegmentDetailResponse(
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
    int recurrenceIndex,
    int inspectorSignal,
    double recentRainfallMm,
    double averageTemperatureCelsius,
    double humidityPercent,
    double mapX,
    double mapY,
    String notes,
    double score,
    PriorityLevel priorityLevel,
    List<String> reasons
) {
}
