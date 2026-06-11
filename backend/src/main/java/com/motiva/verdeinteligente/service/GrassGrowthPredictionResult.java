package com.motiva.verdeinteligente.service;

import java.util.List;

public record GrassGrowthPredictionResult(
    double predictedGrassHeightCm,
    double expectedDailyGrowthCm,
    int daysToCriticalHeight,
    double confidenceScore,
    String modelVersion,
    List<String> drivers
) {
}
