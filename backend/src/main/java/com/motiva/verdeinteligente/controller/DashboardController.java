package com.motiva.verdeinteligente.controller;

import com.motiva.verdeinteligente.dto.DashboardOverviewResponse;
import com.motiva.verdeinteligente.service.ReportingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final ReportingService reportingService;

    public DashboardController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    @GetMapping("/overview")
    public DashboardOverviewResponse overview() {
        return reportingService.dashboardOverview();
    }
}
