package com.motiva.verdeinteligente.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.motiva.verdeinteligente.model.OperationalCriticality;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.model.RoadSegment;
import com.motiva.verdeinteligente.model.VegetationClass;
import com.motiva.verdeinteligente.model.VegetationRuleProfile;
import com.motiva.verdeinteligente.repository.VegetationRuleProfileRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PriorityScoringServiceTest {

    @Mock
    private VegetationRuleProfileRepository vegetationRuleProfileRepository;

    private PriorityScoringService priorityScoringService;

    @BeforeEach
    void setUp() {
        priorityScoringService = new PriorityScoringService(vegetationRuleProfileRepository);
    }

    @Test
    void shouldIncreasePriorityForLongDelayAndHeavyRain() {
        when(vegetationRuleProfileRepository.findByVegetationClass(VegetationClass.AGGRESSIVE))
            .thenReturn(Optional.of(profile(VegetationClass.AGGRESSIVE, 2.0, 20)));

        PriorityScoreResult result = priorityScoringService.calculate(segment(40, 60, 5), 10, 0);

        assertThat(result.score()).isGreaterThanOrEqualTo(75);
        assertThat(result.priorityLevel()).isEqualTo(PriorityLevel.CRITICAL);
        assertThat(result.reasons()).isNotEmpty();
    }

    @Test
    void shouldReducePriorityForRecentlyMaintainedSegment() {
        when(vegetationRuleProfileRepository.findByVegetationClass(VegetationClass.CONTROLLED))
            .thenReturn(Optional.of(profile(VegetationClass.CONTROLLED, 1.0, 40)));

        PriorityScoreResult result = priorityScoringService.calculate(segment(5, 12, 1), 0, 0);

        assertThat(result.score()).isLessThan(55);
        assertThat(result.priorityLevel()).isNotEqualTo(PriorityLevel.CRITICAL);
    }

    private VegetationRuleProfile profile(VegetationClass vegetationClass, double growthWeight, int cycle) {
        VegetationRuleProfile profile = new VegetationRuleProfile();
        profile.setVegetationClass(vegetationClass);
        profile.setGrowthWeight(growthWeight);
        profile.setIdealMaintenanceCycleDays(cycle);
        return profile;
    }

    private RoadSegment segment(int daysAgo, double rainfall, int inspectorSignal) {
        RoadSegment segment = new RoadSegment();
        segment.setLastMowingDate(LocalDate.now().minusDays(daysAgo));
        segment.setRecentRainfallMm(rainfall);
        segment.setAverageTemperatureCelsius(29);
        segment.setHumidityPercent(80);
        segment.setRecurrenceIndex(5);
        segment.setSensitiveArea(true);
        segment.setContractualPressure(true);
        segment.setInspectorSignal(inspectorSignal);
        segment.setVegetationClass(daysAgo > 20 ? VegetationClass.AGGRESSIVE : VegetationClass.CONTROLLED);
        segment.setOperationalCriticality(OperationalCriticality.HIGH);
        return segment;
    }
}
