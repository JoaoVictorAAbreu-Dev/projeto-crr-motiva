package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.dto.CriticalSegmentReportItem;
import com.motiva.verdeinteligente.dto.DashboardOverviewResponse;
import com.motiva.verdeinteligente.dto.EfficiencySummaryResponse;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.repository.PriorityAssessmentRepository;
import com.motiva.verdeinteligente.repository.RoadSegmentRepository;
import com.motiva.verdeinteligente.repository.TeamRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReportingService {

    private static final double REFERENCE_AREA_SQM_PER_SEGMENT = 18_000.0;
    private static final double MANUAL_MOWING_COST_PER_SQM = 0.30;
    private static final double MECHANICAL_MOWING_COST_PER_SQM = 0.165;
    private static final int REFERENCE_FIXED_CYCLES_PER_YEAR = 18;
    private static final int REFERENCE_SMART_CYCLES_PER_YEAR = 14;

    private final PriorityAssessmentRepository priorityAssessmentRepository;
    private final RoadSegmentRepository roadSegmentRepository;
    private final TeamRepository teamRepository;

    public ReportingService(
        PriorityAssessmentRepository priorityAssessmentRepository,
        RoadSegmentRepository roadSegmentRepository,
        TeamRepository teamRepository
    ) {
        this.priorityAssessmentRepository = priorityAssessmentRepository;
        this.roadSegmentRepository = roadSegmentRepository;
        this.teamRepository = teamRepository;
    }

    public List<CriticalSegmentReportItem> criticalSegments() {
        return priorityAssessmentRepository.findByPriorityLevelOrderByScoreDesc(PriorityLevel.CRITICAL).stream()
            .map(assessment -> new CriticalSegmentReportItem(
                assessment.getRoadSegment().getName(),
                assessment.getRoadSegment().getHighway(),
                assessment.getScore(),
                assessment.getPriorityLevel(),
                assessment.getReasons().replace("|", " | ")
            ))
            .toList();
    }

    public EfficiencySummaryResponse efficiencySummary() {
        List<com.motiva.verdeinteligente.model.PriorityAssessment> all = priorityAssessmentRepository.findAllByOrderByScoreDesc();
        long critical = all.stream().filter(item -> item.getPriorityLevel() == PriorityLevel.CRITICAL).count();
        long high = all.stream().filter(item -> item.getPriorityLevel() == PriorityLevel.HIGH).count();

        double blendedCostPerCycle = REFERENCE_AREA_SQM_PER_SEGMENT * ((MANUAL_MOWING_COST_PER_SQM + MECHANICAL_MOWING_COST_PER_SQM) / 2.0);
        double fixedCost = all.size() * blendedCostPerCycle * (REFERENCE_FIXED_CYCLES_PER_YEAR / 12.0);
        double smartSegmentsFactor = all.stream()
            .filter(item -> item.getPriorityLevel() == PriorityLevel.CRITICAL || item.getPriorityLevel() == PriorityLevel.HIGH)
            .count();
        double mediumSegmentsFactor = all.stream()
            .filter(item -> item.getPriorityLevel() == PriorityLevel.MEDIUM)
            .count() * 0.45;
        double smartCost = (smartSegmentsFactor + mediumSegmentsFactor) * blendedCostPerCycle * (REFERENCE_SMART_CYCLES_PER_YEAR / 12.0);
        double savings = fixedCost - smartCost;

        return new EfficiencySummaryResponse(
            (int) critical,
            (int) high,
            fixedCost,
            smartCost,
            savings,
            "Using simulated but consistent premises, the platform prioritizes safety-critical and contract-sensitive segments, reduces unnecessary mowing cycles, and improves crew, equipment, and logistics allocation."
        );
    }

    public DashboardOverviewResponse dashboardOverview() {
        List<CriticalSegmentReportItem> topCritical = priorityAssessmentRepository.findAllByOrderByScoreDesc().stream()
            .limit(5)
            .map(assessment -> new CriticalSegmentReportItem(
                assessment.getRoadSegment().getName(),
                assessment.getRoadSegment().getHighway(),
                assessment.getScore(),
                assessment.getPriorityLevel(),
                assessment.getReasons().replace("|", " | ")
            ))
            .toList();

        List<com.motiva.verdeinteligente.model.PriorityAssessment> all = priorityAssessmentRepository.findAllByOrderByScoreDesc();
        double averageScore = all.stream().mapToDouble(com.motiva.verdeinteligente.model.PriorityAssessment::getScore).average().orElse(0);
        EfficiencySummaryResponse summary = efficiencySummary();

        return new DashboardOverviewResponse(
            roadSegmentRepository.findAll().size(),
            summary.criticalSegments(),
            summary.highPrioritySegments(),
            teamRepository.findAll().size(),
            Math.round(averageScore * 10.0) / 10.0,
            summary.estimatedSavings(),
            topCritical
        );
    }
}
