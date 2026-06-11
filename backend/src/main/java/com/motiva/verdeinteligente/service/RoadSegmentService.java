package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.dto.SegmentDetailResponse;
import com.motiva.verdeinteligente.dto.SegmentSummaryResponse;
import com.motiva.verdeinteligente.exception.ResourceNotFoundException;
import com.motiva.verdeinteligente.model.PriorityAssessment;
import com.motiva.verdeinteligente.model.RoadSegment;
import com.motiva.verdeinteligente.repository.PriorityAssessmentRepository;
import com.motiva.verdeinteligente.repository.RoadSegmentRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class RoadSegmentService {

    private final RoadSegmentRepository roadSegmentRepository;
    private final PriorityAssessmentRepository priorityAssessmentRepository;

    public RoadSegmentService(
        RoadSegmentRepository roadSegmentRepository,
        PriorityAssessmentRepository priorityAssessmentRepository
    ) {
        this.roadSegmentRepository = roadSegmentRepository;
        this.priorityAssessmentRepository = priorityAssessmentRepository;
    }

    public List<SegmentSummaryResponse> findAll() {
        return roadSegmentRepository.findAll().stream()
            .map(this::toSummary)
            .toList();
    }

    public SegmentDetailResponse findByPublicId(UUID id) {
        RoadSegment segment = getEntity(id);
        PriorityAssessment assessment = priorityAssessmentRepository.findByRoadSegment(segment)
            .orElseThrow(() -> new ResourceNotFoundException("Priority assessment not found for segment " + id));

        return new SegmentDetailResponse(
            segment.getPublicId(),
            segment.getHighway(),
            segment.getName(),
            segment.getKmStart(),
            segment.getKmEnd(),
            segment.getExtensionKm(),
            segment.getVegetationClass(),
            segment.getOperationalCriticality(),
            segment.getLastMowingDate(),
            segment.getHistoricalFrequencyDays(),
            segment.isSensitiveArea(),
            segment.isContractualPressure(),
            segment.getRecurrenceIndex(),
            segment.getInspectorSignal(),
            segment.getRecentRainfallMm(),
            segment.getAverageTemperatureCelsius(),
            segment.getHumidityPercent(),
            segment.getMapX(),
            segment.getMapY(),
            segment.getLatitude(),
            segment.getLongitude(),
            segment.getNotes(),
            assessment.getScore(),
            assessment.getPriorityLevel(),
            List.of(assessment.getReasons().split("\\|"))
        );
    }

    public RoadSegment getEntity(UUID id) {
        return roadSegmentRepository.findByPublicId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Segment not found: " + id));
    }

    private SegmentSummaryResponse toSummary(RoadSegment segment) {
        PriorityAssessment assessment = priorityAssessmentRepository.findByRoadSegment(segment)
            .orElseThrow(() -> new ResourceNotFoundException("Priority assessment not found for segment " + segment.getPublicId()));

        return new SegmentSummaryResponse(
            segment.getPublicId(),
            segment.getHighway(),
            segment.getName(),
            segment.getKmStart(),
            segment.getKmEnd(),
            segment.getExtensionKm(),
            segment.getVegetationClass(),
            segment.getOperationalCriticality(),
            segment.getLastMowingDate(),
            segment.getHistoricalFrequencyDays(),
            segment.isSensitiveArea(),
            segment.isContractualPressure(),
            segment.getMapX(),
            segment.getMapY(),
            segment.getLatitude(),
            segment.getLongitude(),
            assessment.getScore(),
            assessment.getPriorityLevel(),
            assessment.getReasons().replace("|", " | ")
        );
    }
}
