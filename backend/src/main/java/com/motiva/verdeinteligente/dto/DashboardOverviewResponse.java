package com.motiva.verdeinteligente.dto;

import java.util.List;

public record DashboardOverviewResponse(
    int totalSegments,
    int criticalSegments,
    int highPrioritySegments,
    int availableTeams,
    int segmentsAtContractRisk,
    int segmentsInSensitiveAreas,
    double averageScore,
    double estimatedSavings,
    int recommendedCrewUtilization,
    String currentScenarioLabel,
    List<CriticalSegmentReportItem> topCriticalSegments
) {
}
