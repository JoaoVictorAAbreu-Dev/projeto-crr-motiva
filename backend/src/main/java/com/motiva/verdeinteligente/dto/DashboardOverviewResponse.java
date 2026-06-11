package com.motiva.verdeinteligente.dto;

import java.util.List;

public record DashboardOverviewResponse(
    int totalSegments,
    int criticalSegments,
    int highPrioritySegments,
    int availableTeams,
    double averageScore,
    double estimatedSavings,
    List<CriticalSegmentReportItem> topCriticalSegments
) {
}
