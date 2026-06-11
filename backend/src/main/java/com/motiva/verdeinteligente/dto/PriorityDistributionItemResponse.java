package com.motiva.verdeinteligente.dto;

import com.motiva.verdeinteligente.model.PriorityLevel;

public record PriorityDistributionItemResponse(
    PriorityLevel priorityLevel,
    int segmentCount,
    String interpretation
) {
}
