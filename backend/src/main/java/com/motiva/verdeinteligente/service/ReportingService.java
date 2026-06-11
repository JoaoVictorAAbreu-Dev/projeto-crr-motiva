package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.dto.CriticalSegmentReportItem;
import com.motiva.verdeinteligente.dto.DashboardAssumptionsResponse;
import com.motiva.verdeinteligente.dto.DashboardOverviewResponse;
import com.motiva.verdeinteligente.dto.EfficiencySummaryResponse;
import com.motiva.verdeinteligente.model.PriorityAssessment;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.model.RoadSegment;
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
            .map(this::toCriticalItem)
            .toList();
    }

    public EfficiencySummaryResponse efficiencySummary() {
        List<PriorityAssessment> all = priorityAssessmentRepository.findAllByOrderByScoreDesc();
        long critical = all.stream().filter(item -> item.getPriorityLevel() == PriorityLevel.CRITICAL).count();
        long high = all.stream().filter(item -> item.getPriorityLevel() == PriorityLevel.HIGH).count();
        long medium = all.stream().filter(item -> item.getPriorityLevel() == PriorityLevel.MEDIUM).count();

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
            (int) medium,
            fixedCost,
            smartCost,
            savings,
            Math.max(0, (REFERENCE_FIXED_CYCLES_PER_YEAR - REFERENCE_SMART_CYCLES_PER_YEAR) * all.size()),
            "Prioritize visibility, contract pressure, and crew logistics before expanding scope.",
            "Using simulated but consistent premises, the platform prioritizes safety-critical and contract-sensitive segments, reduces unnecessary mowing cycles, and improves crew, equipment, and logistics allocation."
        );
    }

    public DashboardOverviewResponse dashboardOverview() {
        List<CriticalSegmentReportItem> topCritical = priorityAssessmentRepository.findAllByOrderByScoreDesc().stream()
            .limit(5)
            .map(this::toCriticalItem)
            .toList();

        List<PriorityAssessment> all = priorityAssessmentRepository.findAllByOrderByScoreDesc();
        List<RoadSegment> segments = roadSegmentRepository.findAll();
        double averageScore = all.stream().mapToDouble(PriorityAssessment::getScore).average().orElse(0);
        EfficiencySummaryResponse summary = efficiencySummary();
        int contractRisk = (int) segments.stream().filter(RoadSegment::isContractualPressure).count();
        int sensitiveAreas = (int) segments.stream().filter(RoadSegment::isSensitiveArea).count();
        int recommendedCrewUtilization = Math.min(100, (summary.criticalSegments() + summary.highPrioritySegments()) * 12);

        return new DashboardOverviewResponse(
            segments.size(),
            summary.criticalSegments(),
            summary.highPrioritySegments(),
            teamRepository.findAll().size(),
            contractRisk,
            sensitiveAreas,
            Math.round(averageScore * 10.0) / 10.0,
            summary.estimatedSavings(),
            recommendedCrewUtilization,
            "Simulated operational scenario based on climate, recurrence, safety, and contractual pressure.",
            topCritical
        );
    }

    public DashboardAssumptionsResponse dashboardAssumptions() {
        return new DashboardAssumptionsResponse(
            "The MVP uses simulated but consistent data, which is acceptable under Motiva's challenge guidance.",
            "The recommendation engine favors explainable operational prioritization over black-box automation.",
            "Safety variables explicitly cover visibility, fire-prevention urgency, and roadside access conditions.",
            "Motiva cameras are not part of the MVP because of LGPD and data-sensitivity constraints.",
            List.of(
                "Manual mowing reference: R$ 0,20 to R$ 0,40 per m2",
                "Mechanical mowing reference: R$ 0,10 to R$ 0,23 per m2"
            ),
            List.of(
                "Reference cycle range: 13 to 18 mowing cycles per year",
                "Dynamic planning should improve crew, equipment, and logistics allocation"
            )
        );
    }

    private CriticalSegmentReportItem toCriticalItem(PriorityAssessment assessment) {
        return new CriticalSegmentReportItem(
            assessment.getRoadSegment().getName(),
            assessment.getRoadSegment().getHighway(),
            assessment.getScore(),
            assessment.getPriorityLevel(),
            assessment.getRoadSegment().isSensitiveArea(),
            assessment.getRoadSegment().isContractualPressure(),
            actionBiasFor(assessment.getPriorityLevel()),
            assessment.getReasons().replace("|", " | ")
        );
    }

    private String actionBiasFor(PriorityLevel priorityLevel) {
        return switch (priorityLevel) {
            case CRITICAL -> "Immediate dispatch";
            case HIGH -> "Plan this week";
            case MEDIUM -> "Monitor with crew reserve";
            case LOW -> "Keep under surveillance";
        };
    }
}
