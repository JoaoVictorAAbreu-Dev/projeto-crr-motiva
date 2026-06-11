package com.motiva.verdeinteligente.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.motiva.verdeinteligente.dto.CriticalSegmentReportItem;
import com.motiva.verdeinteligente.dto.EfficiencySummaryResponse;
import com.motiva.verdeinteligente.dto.OperationalAlertResponse;
import com.motiva.verdeinteligente.dto.PriorityDistributionItemResponse;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.service.ReportingService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReportsController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportingService reportingService;

    @Test
    void shouldReturnPriorityDistribution() throws Exception {
        when(reportingService.priorityDistribution()).thenReturn(
            List.of(new PriorityDistributionItemResponse(PriorityLevel.CRITICAL, 3, "Immediate"))
        );

        mockMvc.perform(get("/api/reports/priority-distribution"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].segmentCount").value(3));
    }

    @Test
    void shouldReturnOperationalAlerts() throws Exception {
        when(reportingService.operationalAlerts()).thenReturn(
            List.of(new OperationalAlertResponse("Contract-sensitive backlog", "HIGH", "3 segments"))
        );

        mockMvc.perform(get("/api/reports/operational-alerts"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].severity").value("HIGH"));
    }

    @Test
    void shouldReturnExistingEfficiencySummary() throws Exception {
        when(reportingService.efficiencySummary()).thenReturn(
            new EfficiencySummaryResponse(2, 3, 1, 1000, 800, 200, 12, "Focus", "Summary")
        );
        when(reportingService.criticalSegments()).thenReturn(
            List.of(new CriticalSegmentReportItem("Trecho A", "Rodoanel", 80, PriorityLevel.CRITICAL, true, true, "Immediate dispatch", "Reason"))
        );

        mockMvc.perform(get("/api/reports/efficiency-summary"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.projectedAnnualCyclesAvoided").value(12));
    }
}
