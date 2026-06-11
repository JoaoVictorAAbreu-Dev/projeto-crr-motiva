package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.model.OperationalCriticality;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.model.RoadSegment;
import com.motiva.verdeinteligente.model.VegetationRuleProfile;
import com.motiva.verdeinteligente.repository.VegetationRuleProfileRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PriorityScoringService {

    private final VegetationRuleProfileRepository ruleProfileRepository;

    public PriorityScoringService(VegetationRuleProfileRepository ruleProfileRepository) {
        this.ruleProfileRepository = ruleProfileRepository;
    }

    public PriorityScoreResult calculate(RoadSegment segment, double rainfallDeltaMm, int inspectorBoost) {
        VegetationRuleProfile profile = ruleProfileRepository.findByVegetationClass(segment.getVegetationClass())
            .orElseThrow(() -> new IllegalStateException("Missing vegetation rule profile for " + segment.getVegetationClass()));

        long daysSinceLastMowing = ChronoUnit.DAYS.between(segment.getLastMowingDate(), LocalDate.now());
        double cyclePressure = Math.min(30.0, (daysSinceLastMowing * 30.0) / profile.getIdealMaintenanceCycleDays());
        double rainfallPressure = Math.min(15.0, (segment.getRecentRainfallMm() + rainfallDeltaMm) / 4.0);
        double temperaturePressure = segment.getAverageTemperatureCelsius() >= 28 ? 6.0 : 3.0;
        double humidityPressure = segment.getHumidityPercent() >= 75 ? 4.0 : 2.0;
        double recurrencePressure = Math.min(10.0, segment.getRecurrenceIndex() * 1.5);
        double sensitiveAreaPressure = segment.isSensitiveArea() ? 10.0 : 0.0;
        double contractualPressure = segment.isContractualPressure() ? 8.0 : 0.0;
        double inspectorPressure = Math.min(10.0, (segment.getInspectorSignal() + inspectorBoost) * 2.0);
        double growthPressure = profile.getGrowthWeight() * 8.0;
        double operationalPressure = operationalWeight(segment.getOperationalCriticality());

        double rawScore = cyclePressure + rainfallPressure + temperaturePressure + humidityPressure + recurrencePressure
            + sensitiveAreaPressure + contractualPressure + inspectorPressure + growthPressure + operationalPressure;
        double score = Math.min(100.0, Math.round(rawScore * 10.0) / 10.0);

        List<String> reasons = new ArrayList<>();
        if (cyclePressure >= 20) {
            reasons.add("Long interval since the last mowing increased urgency.");
        }
        if (rainfallPressure >= 10) {
            reasons.add("Recent rainfall indicates accelerated vegetation growth.");
        }
        if (temperaturePressure >= 6.0 && humidityPressure >= 4.0) {
            reasons.add("Heat and humidity conditions increase regrowth speed and fire-prevention urgency.");
        }
        if (sensitiveAreaPressure > 0) {
            reasons.add("Sensitive roadside area requires preventive attention for visibility and infrastructure protection.");
        }
        if (contractualPressure > 0) {
            reasons.add("Contractual obligations raise the operational priority.");
        }
        if (inspectorPressure >= 8) {
            reasons.add("Field inspection signaled visible risk escalation.");
        }
        if (segment.getOperationalCriticality() == OperationalCriticality.HIGH) {
            reasons.add("High operational criticality can affect driver safety, visibility, and roadside response access.");
        }
        if (reasons.isEmpty()) {
            reasons.add("Segment remains under monitored operational control.");
        }

        return new PriorityScoreResult(score, classify(score), reasons);
    }

    private double operationalWeight(OperationalCriticality criticality) {
        return switch (criticality) {
            case LOW -> 4.0;
            case MEDIUM -> 8.0;
            case HIGH -> 12.0;
        };
    }

    private PriorityLevel classify(double score) {
        if (score >= 75) {
            return PriorityLevel.CRITICAL;
        }
        if (score >= 55) {
            return PriorityLevel.HIGH;
        }
        if (score >= 35) {
            return PriorityLevel.MEDIUM;
        }
        return PriorityLevel.LOW;
    }
}
