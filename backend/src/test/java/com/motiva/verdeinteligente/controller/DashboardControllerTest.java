package com.motiva.verdeinteligente.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.motiva.verdeinteligente.dto.DashboardAssumptionsResponse;
import com.motiva.verdeinteligente.dto.DashboardOverviewResponse;
import com.motiva.verdeinteligente.service.ReportingService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportingService reportingService;

    @Test
    void shouldReturnDashboardOverview() throws Exception {
        when(reportingService.dashboardOverview()).thenReturn(
            new DashboardOverviewResponse(8, 3, 2, 4, 4, 5, 68.4, 12400, 60, "Simulated", List.of())
        );

        mockMvc.perform(get("/api/dashboard/overview"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.criticalSegments").value(3))
            .andExpect(jsonPath("$.segmentsAtContractRisk").value(4));
    }

    @Test
    void shouldReturnDashboardAssumptions() throws Exception {
        when(reportingService.dashboardAssumptions()).thenReturn(
            new DashboardAssumptionsResponse("Simulated", "Explainable", "Safety first", "No cameras", List.of("ref"), List.of("cycles"))
        );

        mockMvc.perform(get("/api/dashboard/assumptions"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.privacyPolicy").value("No cameras"));
    }
}
