package com.motiva.verdeinteligente.controller;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motiva.verdeinteligente.dto.PriorityAssessmentResponse;
import com.motiva.verdeinteligente.dto.RecalculatePrioritiesRequest;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.service.PriorityAssessmentService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PriorityAssessmentController.class)
class PriorityAssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PriorityAssessmentService priorityAssessmentService;

    @Test
    void shouldReturnPriorityRanking() throws Exception {
        when(priorityAssessmentService.ranking()).thenReturn(List.of(
            new PriorityAssessmentResponse(UUID.randomUUID(), "Trecho A", 88.0, PriorityLevel.CRITICAL, List.of("Rainfall"))
        ));

        mockMvc.perform(get("/api/priority-ranking"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].segmentName").value("Trecho A"))
            .andExpect(jsonPath("$[0].priorityLevel").value("CRITICAL"));
    }

    @Test
    void shouldRecalculatePriorityRanking() throws Exception {
        RecalculatePrioritiesRequest request = new RecalculatePrioritiesRequest(18.0, 2);
        when(priorityAssessmentService.recalculateAll(18.0, 2)).thenReturn(List.of(
            new PriorityAssessmentResponse(UUID.randomUUID(), "Trecho B", 64.0, PriorityLevel.HIGH, List.of("Inspector"))
        ));

        mockMvc.perform(post("/api/priority-assessments/recalculate")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].score").value(64.0))
            .andExpect(jsonPath("$[0].priorityLevel").value("HIGH"));
    }

    @Test
    void shouldRejectInvalidRecalculateRequest() throws Exception {
        mockMvc.perform(post("/api/priority-assessments/recalculate")
                .contentType(APPLICATION_JSON)
                .content("""
                    {"rainfallDeltaMm":180,"inspectorSignalBoost":8}
                    """))
            .andExpect(status().isBadRequest());
    }
}
