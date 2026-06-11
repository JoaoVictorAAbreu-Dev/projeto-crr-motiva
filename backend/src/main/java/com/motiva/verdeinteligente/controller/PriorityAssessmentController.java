package com.motiva.verdeinteligente.controller;

import com.motiva.verdeinteligente.dto.PriorityAssessmentResponse;
import com.motiva.verdeinteligente.dto.RecalculatePrioritiesRequest;
import com.motiva.verdeinteligente.service.PriorityAssessmentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PriorityAssessmentController {

    private final PriorityAssessmentService priorityAssessmentService;

    public PriorityAssessmentController(PriorityAssessmentService priorityAssessmentService) {
        this.priorityAssessmentService = priorityAssessmentService;
    }

    @GetMapping("/priority-ranking")
    public List<PriorityAssessmentResponse> ranking() {
        return priorityAssessmentService.ranking();
    }

    @PostMapping("/priority-assessments/recalculate")
    public List<PriorityAssessmentResponse> recalculate(@Valid @RequestBody RecalculatePrioritiesRequest request) {
        return priorityAssessmentService.recalculateAll(request.rainfallDeltaMm(), request.inspectorSignalBoost());
    }
}
