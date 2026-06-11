package com.motiva.verdeinteligente.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.motiva.verdeinteligente.dto.WeeklyPlanRequest;
import com.motiva.verdeinteligente.dto.WeeklyPlanResponse;
import com.motiva.verdeinteligente.model.PriorityAssessment;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.model.RoadSegment;
import com.motiva.verdeinteligente.model.Team;
import com.motiva.verdeinteligente.repository.PriorityAssessmentRepository;
import com.motiva.verdeinteligente.repository.TeamRepository;
import com.motiva.verdeinteligente.repository.WeeklyPlanRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WeeklyPlanServiceTest {

    @Mock
    private PriorityScoringService priorityScoringService;
    @Mock
    private PriorityAssessmentRepository priorityAssessmentRepository;
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private WeeklyPlanRepository weeklyPlanRepository;

    private WeeklyPlanService weeklyPlanService;

    @BeforeEach
    void setUp() {
        weeklyPlanService = new WeeklyPlanService(
            priorityScoringService,
            priorityAssessmentRepository,
            teamRepository,
            weeklyPlanRepository
        );
    }

    @Test
    void shouldNotExceedTeamCapacity() {
        PriorityAssessment assessment = assessment("Trecho A", 88);
        Team team = new Team();
        team.setName("Equipe Alfa");
        team.setBaseHighway("Rodoanel Oeste");
        team.setWeeklyCapacityHours(12);

        when(teamRepository.findAllByOrderByNameAsc()).thenReturn(List.of(team));
        when(priorityAssessmentRepository.findAll()).thenReturn(List.of(assessment));
        when(priorityScoringService.calculate(assessment.getRoadSegment(), 0.0, 0))
            .thenReturn(new PriorityScoreResult(88, PriorityLevel.CRITICAL, List.of("Critical")));

        WeeklyPlanResponse response = weeklyPlanService.generate(new WeeklyPlanRequest(LocalDate.now(), 1, 0.0));

        assertThat(response.totalAssignedHours()).isLessThanOrEqualTo(12);
        assertThat(response.items()).hasSize(1);
    }

    private PriorityAssessment assessment(String name, double score) {
        RoadSegment segment = new RoadSegment();
        segment.setName(name);
        segment.setHighway("Rodoanel Oeste");
        segment.setExtensionKm(3);

        PriorityAssessment assessment = new PriorityAssessment();
        assessment.setRoadSegment(segment);
        assessment.setScore(score);
        assessment.setPriorityLevel(PriorityLevel.CRITICAL);
        assessment.setReasons("Critical");
        return assessment;
    }
}
