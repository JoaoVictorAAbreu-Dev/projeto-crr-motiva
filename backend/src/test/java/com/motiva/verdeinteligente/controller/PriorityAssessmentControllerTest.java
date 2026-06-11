package com.motiva.verdeinteligente.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.motiva.verdeinteligente.dto.PriorityAssessmentResponse;
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

    @MockBean
    private PriorityAssessmentService priorityAssessmentService;

    @Test
    void shouldReturnPriorityRanking() throws Exception {
        when(priorityAssessmentService.ranking()).thenReturn(List.of(
            new PriorityAssessmentResponse(UUID.randomUUID(), "Trecho Osasco Sul", 83.5, com.motiva.verdeinteligente.model.PriorityLevel.CRITICAL, List.of("Heavy rainfall"))
        ));

        mockMvc.perform(get("/api/priority-ranking"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].segmentName").value("Trecho Osasco Sul"))
            .andExpect(jsonPath("$[0].priorityLevel").value("CRITICAL"));
    }
}
