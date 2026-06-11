package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.dto.PriorityAssessmentResponse;
import com.motiva.verdeinteligente.model.PriorityAssessment;
import com.motiva.verdeinteligente.model.RoadSegment;
import com.motiva.verdeinteligente.repository.PriorityAssessmentRepository;
import com.motiva.verdeinteligente.repository.RoadSegmentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PriorityAssessmentService {

    private final RoadSegmentRepository roadSegmentRepository;
    private final PriorityAssessmentRepository priorityAssessmentRepository;
    private final PriorityScoringService priorityScoringService;

    public PriorityAssessmentService(
        RoadSegmentRepository roadSegmentRepository,
        PriorityAssessmentRepository priorityAssessmentRepository,
        PriorityScoringService priorityScoringService
    ) {
        this.roadSegmentRepository = roadSegmentRepository;
        this.priorityAssessmentRepository = priorityAssessmentRepository;
        this.priorityScoringService = priorityScoringService;
    }

    @Transactional
    public List<PriorityAssessmentResponse> recalculateAll(double rainfallDeltaMm, int inspectorBoost) {
        return roadSegmentRepository.findAll().stream()
            .map(segment -> calculateAndPersist(segment, rainfallDeltaMm, inspectorBoost))
            .toList();
    }

    public List<PriorityAssessmentResponse> ranking() {
        return priorityAssessmentRepository.findAllByOrderByScoreDesc().stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional
    public PriorityAssessment calculateAndStoreForSegment(RoadSegment segment) {
        calculateAndPersist(segment, 0, 0);
        return priorityAssessmentRepository.findByRoadSegment(segment).orElseThrow();
    }

    private PriorityAssessmentResponse calculateAndPersist(RoadSegment segment, double rainfallDeltaMm, int inspectorBoost) {
        PriorityScoreResult result = priorityScoringService.calculate(segment, rainfallDeltaMm, inspectorBoost);

        PriorityAssessment assessment = priorityAssessmentRepository.findByRoadSegment(segment)
            .orElseGet(PriorityAssessment::new);
        assessment.setRoadSegment(segment);
        assessment.setScore(result.score());
        assessment.setPriorityLevel(result.priorityLevel());
        assessment.setReasons(String.join("|", result.reasons()));
        assessment.setCalculatedAt(LocalDateTime.now());
        priorityAssessmentRepository.save(assessment);

        return toResponse(assessment);
    }

    private PriorityAssessmentResponse toResponse(PriorityAssessment assessment) {
        return new PriorityAssessmentResponse(
            assessment.getRoadSegment().getPublicId(),
            assessment.getRoadSegment().getName(),
            assessment.getScore(),
            assessment.getPriorityLevel(),
            List.of(assessment.getReasons().split("\\|"))
        );
    }
}
