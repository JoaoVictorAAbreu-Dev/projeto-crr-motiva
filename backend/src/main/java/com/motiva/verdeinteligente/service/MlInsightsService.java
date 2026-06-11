package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.dto.GrassGrowthPredictionResponse;
import com.motiva.verdeinteligente.dto.GrassGrowthRankingItemResponse;
import com.motiva.verdeinteligente.exception.ResourceNotFoundException;
import com.motiva.verdeinteligente.model.RoadSegment;
import com.motiva.verdeinteligente.repository.RoadSegmentRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class MlInsightsService {

    private final RoadSegmentRepository roadSegmentRepository;
    private final GrassGrowthModelService grassGrowthModelService;

    public MlInsightsService(
        RoadSegmentRepository roadSegmentRepository,
        GrassGrowthModelService grassGrowthModelService
    ) {
        this.roadSegmentRepository = roadSegmentRepository;
        this.grassGrowthModelService = grassGrowthModelService;
    }

    public GrassGrowthPredictionResponse prediction(UUID segmentId) {
        RoadSegment segment = roadSegmentRepository.findByPublicId(segmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Segment not found: " + segmentId));
        GrassGrowthPredictionResult prediction = grassGrowthModelService.predict(segment);
        return toResponse(segment, prediction);
    }

    public List<GrassGrowthRankingItemResponse> ranking() {
        return roadSegmentRepository.findAll().stream()
            .map(segment -> Map.entry(segment, grassGrowthModelService.predict(segment)))
            .sorted(Comparator.comparingInt(entry -> entry.getValue().daysToCriticalHeight()))
            .limit(6)
            .map(entry -> new GrassGrowthRankingItemResponse(
                entry.getKey().getPublicId(),
                entry.getKey().getName(),
                entry.getValue().predictedGrassHeightCm(),
                entry.getValue().daysToCriticalHeight(),
                entry.getValue().daysToCriticalHeight() <= 3 ? "Dispatch a crew immediately." : "Schedule before the next weekly window."
            ))
            .toList();
    }

    private GrassGrowthPredictionResponse toResponse(RoadSegment segment, GrassGrowthPredictionResult prediction) {
        return new GrassGrowthPredictionResponse(
            segment.getPublicId(),
            segment.getName(),
            prediction.predictedGrassHeightCm(),
            prediction.expectedDailyGrowthCm(),
            prediction.daysToCriticalHeight(),
            prediction.confidenceScore(),
            prediction.modelVersion(),
            prediction.drivers()
        );
    }
}
