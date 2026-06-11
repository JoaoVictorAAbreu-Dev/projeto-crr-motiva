package com.motiva.verdeinteligente.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.motiva.verdeinteligente.dto.SegmentDetailResponse;
import com.motiva.verdeinteligente.dto.SegmentSummaryResponse;
import com.motiva.verdeinteligente.model.OperationalCriticality;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.model.VegetationClass;
import com.motiva.verdeinteligente.service.RoadSegmentService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RoadSegmentController.class)
class RoadSegmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoadSegmentService roadSegmentService;

    @Test
    void shouldReturnSegmentSummaries() throws Exception {
        UUID id = UUID.randomUUID();
        when(roadSegmentService.findAll()).thenReturn(List.of(
            new SegmentSummaryResponse(
                id, "Rodoanel Oeste", "Trecho 1", 10.0, 12.0, 2.0, VegetationClass.MODERATE,
                OperationalCriticality.HIGH, LocalDate.of(2026, 6, 1), 18, true, true,
                32.0, 44.0, -23.5, -46.8, 81.4, PriorityLevel.CRITICAL, "Immediate"
            )
        ));

        mockMvc.perform(get("/api/segments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].highway").value("Rodoanel Oeste"))
            .andExpect(jsonPath("$[0].priorityLevel").value("CRITICAL"));
    }

    @Test
    void shouldReturnSegmentDetail() throws Exception {
        UUID id = UUID.randomUUID();
        when(roadSegmentService.findByPublicId(id)).thenReturn(
            new SegmentDetailResponse(
                id, "Rodoanel Oeste", "Trecho 1", 10.0, 12.0, 2.0, VegetationClass.MODERATE,
                OperationalCriticality.HIGH, LocalDate.of(2026, 6, 1), 18, true, true, 5, 2, 44.0, 28.0,
                80.0, 32.0, 44.0, -23.5, -46.8, "Monitorar", 81.4, PriorityLevel.CRITICAL,
                List.of("Heavy rainfall", "Contract pressure")
            )
        );

        mockMvc.perform(get("/api/segments/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Trecho 1"))
            .andExpect(jsonPath("$.reasons[0]").value("Heavy rainfall"));
    }
}
