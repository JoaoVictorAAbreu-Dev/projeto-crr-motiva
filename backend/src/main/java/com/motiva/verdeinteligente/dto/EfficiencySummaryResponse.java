package com.motiva.verdeinteligente.dto;

public record EfficiencySummaryResponse(
    int criticalSegments,
    int highPrioritySegments,
    int mediumPrioritySegments,
    double estimatedWeeklyCostFixedSchedule,
    double estimatedWeeklyCostSmartPlan,
    double estimatedSavings,
    int projectedAnnualCyclesAvoided,
    String operationalFocus,
    String summary
) {
}
