package com.motiva.verdeinteligente.controller;

import com.motiva.verdeinteligente.dto.SegmentDetailResponse;
import com.motiva.verdeinteligente.dto.SegmentSummaryResponse;
import com.motiva.verdeinteligente.service.RoadSegmentService;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/segments")
public class RoadSegmentController {

    private final RoadSegmentService roadSegmentService;

    public RoadSegmentController(RoadSegmentService roadSegmentService) {
        this.roadSegmentService = roadSegmentService;
    }

    @GetMapping
    public List<SegmentSummaryResponse> list() {
        return roadSegmentService.findAll();
    }

    @GetMapping("/{id}")
    public SegmentDetailResponse findById(@PathVariable UUID id) {
        return roadSegmentService.findByPublicId(id);
    }
}
