package com.motiva.verdeinteligente.dto;

import java.time.LocalDate;
import java.util.UUID;

public record LiveWeatherResponse(
    UUID segmentId,
    String segmentName,
    boolean liveData,
    LocalDate snapshotDate,
    double rainfallMm,
    double temperatureCelsius,
    double humidityPercent,
    String source
) {
}
