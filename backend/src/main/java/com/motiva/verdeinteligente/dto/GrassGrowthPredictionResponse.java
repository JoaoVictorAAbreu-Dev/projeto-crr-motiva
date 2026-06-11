package com.motiva.verdeinteligente.dto;

import java.util.List;
import java.util.UUID;

public record GrassGrowthPredictionResponse(
    UUID segmentId,
    String segmentName,
    double predictedGrassHeightCm,
    double expectedDailyGrowthCm,
    int daysToCriticalHeight,
    double confidenceScore,
    String modelVersion,
    List<String> drivers
) {
}
