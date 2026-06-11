package com.motiva.verdeinteligente.controller;

import com.motiva.verdeinteligente.dto.GrassGrowthPredictionResponse;
import com.motiva.verdeinteligente.dto.GrassGrowthRankingItemResponse;
import com.motiva.verdeinteligente.service.MlInsightsService;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ml/grass-growth")
public class MlInsightsController {

    private final MlInsightsService mlInsightsService;

    public MlInsightsController(MlInsightsService mlInsightsService) {
        this.mlInsightsService = mlInsightsService;
    }

    @GetMapping("/{segmentId}")
    public GrassGrowthPredictionResponse prediction(@PathVariable UUID segmentId) {
        return mlInsightsService.prediction(segmentId);
    }

    @GetMapping("/ranking")
    public List<GrassGrowthRankingItemResponse> ranking() {
        return mlInsightsService.ranking();
    }
}
