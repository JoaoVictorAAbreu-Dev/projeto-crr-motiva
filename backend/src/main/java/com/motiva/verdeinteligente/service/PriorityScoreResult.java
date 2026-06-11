package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.model.PriorityLevel;
import java.util.List;

public record PriorityScoreResult(
    double score,
    PriorityLevel priorityLevel,
    List<String> reasons
) {
}
