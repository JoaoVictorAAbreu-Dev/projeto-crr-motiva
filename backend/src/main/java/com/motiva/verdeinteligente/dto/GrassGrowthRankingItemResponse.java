package com.motiva.verdeinteligente.dto;

import java.util.UUID;

public record GrassGrowthRankingItemResponse(
    UUID segmentId,
    String segmentName,
    double predictedGrassHeightCm,
    int daysToCriticalHeight,
    String recommendation
) {
}
