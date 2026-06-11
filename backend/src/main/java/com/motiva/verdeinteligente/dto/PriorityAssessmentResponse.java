package com.motiva.verdeinteligente.dto;

import com.motiva.verdeinteligente.model.PriorityLevel;
import java.util.List;
import java.util.UUID;

public record PriorityAssessmentResponse(
    UUID segmentId,
    String segmentName,
    double score,
    PriorityLevel priorityLevel,
    List<String> reasons
) {
}
