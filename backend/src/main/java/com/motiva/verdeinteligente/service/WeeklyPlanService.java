package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.dto.WeeklyPlanItemResponse;
import com.motiva.verdeinteligente.dto.WeeklyPlanRequest;
import com.motiva.verdeinteligente.dto.WeeklyPlanResponse;
import com.motiva.verdeinteligente.exception.ResourceNotFoundException;
import com.motiva.verdeinteligente.model.PriorityAssessment;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.model.Team;
import com.motiva.verdeinteligente.model.WeeklyPlan;
import com.motiva.verdeinteligente.model.WeeklyPlanItem;
import com.motiva.verdeinteligente.repository.PriorityAssessmentRepository;
import com.motiva.verdeinteligente.repository.TeamRepository;
import com.motiva.verdeinteligente.repository.WeeklyPlanRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WeeklyPlanService {

    private final PriorityScoringService priorityScoringService;
    private final PriorityAssessmentRepository priorityAssessmentRepository;
    private final TeamRepository teamRepository;
    private final WeeklyPlanRepository weeklyPlanRepository;

    public WeeklyPlanService(
        PriorityScoringService priorityScoringService,
        PriorityAssessmentRepository priorityAssessmentRepository,
        TeamRepository teamRepository,
        WeeklyPlanRepository weeklyPlanRepository
    ) {
        this.priorityScoringService = priorityScoringService;
        this.priorityAssessmentRepository = priorityAssessmentRepository;
        this.teamRepository = teamRepository;
        this.weeklyPlanRepository = weeklyPlanRepository;
    }

    @Transactional
    public WeeklyPlanResponse generate(WeeklyPlanRequest request) {
        List<Team> selectedTeams = teamRepository.findAllByOrderByNameAsc().stream()
            .limit(request.crewCount())
            .toList();

        if (selectedTeams.isEmpty()) {
            throw new ResourceNotFoundException("No teams available to generate a weekly plan");
        }

        List<PriorityAssessment> recalculated = priorityAssessmentRepository.findAll().stream()
            .sorted(Comparator.comparing(PriorityAssessment::getScore).reversed())
            .map(assessment -> {
                PriorityScoreResult scenarioResult = priorityScoringService.calculate(
                    assessment.getRoadSegment(),
                    request.scenarioRainfallMm(),
                    0
                );
                assessment.setScore(scenarioResult.score());
                assessment.setPriorityLevel(scenarioResult.priorityLevel());
                assessment.setReasons(String.join("|", scenarioResult.reasons()));
                return assessment;
            })
            .filter(assessment -> assessment.getPriorityLevel() == PriorityLevel.CRITICAL
                || assessment.getPriorityLevel() == PriorityLevel.HIGH)
            .sorted(Comparator.comparing(PriorityAssessment::getScore).reversed())
            .toList();

        WeeklyPlan plan = new WeeklyPlan();
        plan.setStartDate(request.startDate());
        plan.setRequestedCrewCount(request.crewCount());
        plan.setScenarioRainfallMm(request.scenarioRainfallMm());

        Map<Long, Integer> remainingHours = new HashMap<>();
        selectedTeams.forEach(team -> remainingHours.put(team.getId(), team.getWeeklyCapacityHours()));

        int order = 1;
        int assignedHours = 0;
        int assignedSegments = 0;
        List<WeeklyPlanItemResponse> items = new ArrayList<>();

        for (PriorityAssessment assessment : recalculated) {
            int estimatedHours = estimateHours(assessment);
            Team team = pickBestTeam(selectedTeams, remainingHours, assessment);
            if (team == null) {
                continue;
            }

            remainingHours.put(team.getId(), remainingHours.get(team.getId()) - estimatedHours);
            assignedHours += estimatedHours;
            assignedSegments++;

            WeeklyPlanItem item = new WeeklyPlanItem();
            item.setWeeklyPlan(plan);
            item.setRoadSegment(assessment.getRoadSegment());
            item.setTeam(team);
            item.setExecutionOrder(order);
            item.setEstimatedHours(estimatedHours);
            item.setRecommendedWindow(windowFor(assessment.getPriorityLevel()));
            item.setJustification("Assigned due to score %.1f and highway proximity fit.".formatted(assessment.getScore()));
            plan.getItems().add(item);

            items.add(new WeeklyPlanItemResponse(
                order,
                team.getName(),
                assessment.getRoadSegment().getName(),
                assessment.getRoadSegment().getHighway(),
                estimatedHours,
                item.getRecommendedWindow(),
                assessment.getScore(),
                assessment.getPriorityLevel(),
                item.getJustification()
            ));
            order++;
        }

        plan.setTotalAssignedHours(assignedHours);
        plan.setPendingSegments(Math.max(0, recalculated.size() - assignedSegments));
        weeklyPlanRepository.save(plan);

        return new WeeklyPlanResponse(
            plan.getId(),
            plan.getStartDate(),
            plan.getRequestedCrewCount(),
            plan.getScenarioRainfallMm(),
            plan.getTotalAssignedHours(),
            plan.getPendingSegments(),
            items
        );
    }

    public WeeklyPlanResponse findById(Long id) {
        WeeklyPlan plan = weeklyPlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Weekly plan not found: " + id));

        List<WeeklyPlanItemResponse> items = plan.getItems().stream()
            .sorted(Comparator.comparing(WeeklyPlanItem::getExecutionOrder))
            .map(item -> {
                PriorityAssessment assessment = priorityAssessmentRepository.findByRoadSegment(item.getRoadSegment()).orElseThrow();
                return new WeeklyPlanItemResponse(
                    item.getExecutionOrder(),
                    item.getTeam().getName(),
                    item.getRoadSegment().getName(),
                    item.getRoadSegment().getHighway(),
                    item.getEstimatedHours(),
                    item.getRecommendedWindow(),
                    assessment.getScore(),
                    assessment.getPriorityLevel(),
                    item.getJustification()
                );
            })
            .toList();

        return new WeeklyPlanResponse(
            plan.getId(),
            plan.getStartDate(),
            plan.getRequestedCrewCount(),
            plan.getScenarioRainfallMm(),
            plan.getTotalAssignedHours(),
            plan.getPendingSegments(),
            items
        );
    }

    private Team pickBestTeam(List<Team> teams, Map<Long, Integer> remainingHours, PriorityAssessment assessment) {
        return teams.stream()
            .filter(team -> remainingHours.get(team.getId()) >= estimateHours(assessment))
            .sorted(Comparator
                .comparing((Team team) -> !team.getBaseHighway().equalsIgnoreCase(assessment.getRoadSegment().getHighway()))
                .thenComparing(team -> -remainingHours.get(team.getId())))
            .findFirst()
            .orElse(null);
    }

    private int estimateHours(PriorityAssessment assessment) {
        double extension = assessment.getRoadSegment().getExtensionKm();
        return Math.max(4, (int) Math.ceil(extension * 1.6 + assessment.getScore() / 20.0));
    }

    private String windowFor(PriorityLevel priorityLevel) {
        return switch (priorityLevel) {
            case CRITICAL -> "48h";
            case HIGH -> "72h";
            case MEDIUM -> "This week";
            case LOW -> "Monitor";
        };
    }
}
