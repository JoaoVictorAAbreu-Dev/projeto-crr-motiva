package com.motiva.verdeinteligente.controller;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        WeeklyPlanRequest request = new WeeklyPlanRequest(LocalDate.of(2026, 6, 16), 2, 30.0);
        when(weeklyPlanService.generate(request)).thenReturn(response());

        mockMvc.perform(post("/api/weekly-plans/generate")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.requestedCrewCount").value(2))
            .andExpect(jsonPath("$.items[0].teamName").value("Equipe Alfa"));
    }

    @Test
    void shouldReturnWeeklyPlanById() throws Exception {
        when(weeklyPlanService.findById(7L)).thenReturn(response());

        mockMvc.perform(get("/api/weekly-plans/7"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(7))
            .andExpect(jsonPath("$.pendingSegments").value(1));
    }

    @Test
    void shouldRejectInvalidWeeklyPlanRequest() throws Exception {
        mockMvc.perform(post("/api/weekly-plans/generate")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"startDate":"2026-06-16","crewCount":0,"scenarioRainfallMm":150}
                    """))
            .andExpect(status().isBadRequest());
    }

    private WeeklyPlanResponse response() {
        return new WeeklyPlanResponse(
            7L,
            LocalDate.of(2026, 6, 16),
            2,
            30.0,
            10,
            1,
            List.of(new WeeklyPlanItemResponse(
                1,
                "Equipe Alfa",
                "Trecho Norte",
                "Rodoanel Oeste",
                5,
                "Seg 07:00-11:00",
                86.0,
                PriorityLevel.CRITICAL,
                "Priority score and crew proximity"
            ))
        );
    }
}
