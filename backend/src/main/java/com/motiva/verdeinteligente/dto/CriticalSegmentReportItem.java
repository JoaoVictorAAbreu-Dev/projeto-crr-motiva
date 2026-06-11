package com.motiva.verdeinteligente.dto;

import com.motiva.verdeinteligente.model.PriorityLevel;

public record CriticalSegmentReportItem(
    String segmentName,
    String highway,
    double score,
    PriorityLevel priorityLevel,
    String reasons
) {
}
