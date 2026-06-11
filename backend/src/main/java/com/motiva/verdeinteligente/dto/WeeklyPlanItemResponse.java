package com.motiva.verdeinteligente.dto;

import com.motiva.verdeinteligente.model.PriorityLevel;

public record WeeklyPlanItemResponse(
    int executionOrder,
    String teamName,
    String segmentName,
    String highway,
    int estimatedHours,
    String recommendedWindow,
    double score,
    PriorityLevel priorityLevel,
    String justification
) {
}
