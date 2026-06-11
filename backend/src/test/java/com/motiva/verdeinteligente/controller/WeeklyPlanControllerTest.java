package com.motiva.verdeinteligente.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motiva.verdeinteligente.dto.WeeklyPlanItemResponse;
import com.motiva.verdeinteligente.dto.WeeklyPlanRequest;
import com.motiva.verdeinteligente.dto.WeeklyPlanResponse;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.service.WeeklyPlanService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(WeeklyPlanController.class)
class WeeklyPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private WeeklyPlanService weeklyPlanService;

    @Test
    void shouldGenerateWeeklyPlan() throws Exception {
        WeeklyPlanRequest request = new WeeklyPlanRequest(LocalDate.of(2026, 6, 10), 2, 20.0);
        WeeklyPlanResponse response = new WeeklyPlanResponse(
            1L,
            request.startDate(),
            request.crewCount(),
            request.scenarioRainfallMm(),
            18,
            0,
            List.of(new WeeklyPlanItemResponse(1, "Equipe Alfa", "Trecho A", "Rodoanel Oeste", 8, "48h", 82.0, PriorityLevel.CRITICAL, "Assigned"))
        );

        when(weeklyPlanService.generate(request)).thenReturn(response);

        mockMvc.perform(post("/api/weekly-plans/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.requestedCrewCount").value(2))
            .andExpect(jsonPath("$.items[0].teamName").value("Equipe Alfa"));
    }
}
