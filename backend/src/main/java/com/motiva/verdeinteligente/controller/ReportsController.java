package com.motiva.verdeinteligente.controller;

import com.motiva.verdeinteligente.dto.CriticalSegmentReportItem;
import com.motiva.verdeinteligente.dto.EfficiencySummaryResponse;
import com.motiva.verdeinteligente.service.ReportingService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportingService reportingService;

    public ReportsController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/critical-segments")
    public List<CriticalSegmentReportItem> criticalSegments() {
        return reportingService.criticalSegments();
    }

    @GetMapping("/efficiency-summary")
    public EfficiencySummaryResponse efficiencySummary() {
        return reportingService.efficiencySummary();
    }
}
