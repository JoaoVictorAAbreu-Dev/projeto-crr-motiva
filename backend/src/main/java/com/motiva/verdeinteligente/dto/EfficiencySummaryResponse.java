package com.motiva.verdeinteligente.dto;

public record EfficiencySummaryResponse(
    int criticalSegments,
    int highPrioritySegments,
    double estimatedWeeklyCostFixedSchedule,
    double estimatedWeeklyCostSmartPlan,
    double estimatedSavings,
    String summary
) {
}
