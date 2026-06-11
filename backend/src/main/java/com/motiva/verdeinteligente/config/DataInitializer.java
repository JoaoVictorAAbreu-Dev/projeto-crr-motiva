package com.motiva.verdeinteligente.config;

import com.motiva.verdeinteligente.model.*;
import com.motiva.verdeinteligente.repository.*;
import com.motiva.verdeinteligente.service.PriorityAssessmentService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(
        RoadSegmentRepository roadSegmentRepository,
        VegetationRuleProfileRepository vegetationRuleProfileRepository,
        TeamRepository teamRepository,
        WeatherSnapshotRepository weatherSnapshotRepository,
        PriorityAssessmentService priorityAssessmentService
    ) {
        return args -> {
            if (roadSegmentRepository.count() > 0) {
                return;
            }

            VegetationRuleProfile controlled = new VegetationRuleProfile();
            controlled.setVegetationClass(VegetationClass.CONTROLLED);
            controlled.setGrowthWeight(1.0);
            controlled.setIdealMaintenanceCycleDays(40);

            VegetationRuleProfile moderate = new VegetationRuleProfile();
            moderate.setVegetationClass(VegetationClass.MODERATE);
            moderate.setGrowthWeight(1.5);
            moderate.setIdealMaintenanceCycleDays(30);

            VegetationRuleProfile aggressive = new VegetationRuleProfile();
            aggressive.setVegetationClass(VegetationClass.AGGRESSIVE);
            aggressive.setGrowthWeight(2.0);
            aggressive.setIdealMaintenanceCycleDays(22);

            vegetationRuleProfileRepository.saveAll(List.of(controlled, moderate, aggressive));

            List<RoadSegment> segments = List.of(
                createSegment("Rodoanel Oeste", "Trecho Barueri Norte", 0.0, 4.8, VegetationClass.MODERATE, OperationalCriticality.HIGH, 32, 28, true, true, 5, 4, 44, 29, 76, 15, 22, "High truck flow and visibility demand."),
                createSegment("Rodoanel Oeste", "Trecho Osasco Sul", 5.0, 9.2, VegetationClass.AGGRESSIVE, OperationalCriticality.HIGH, 39, 24, true, true, 6, 5, 58, 30, 81, 26, 40, "Repeated regrowth near access ramps."),
                createSegment("Rodoanel Oeste", "Trecho Embu das Artes", 9.3, 13.8, VegetationClass.AGGRESSIVE, OperationalCriticality.MEDIUM, 27, 25, false, true, 5, 3, 52, 27, 79, 37, 52, "Fast vegetation growth after rainfall concentration."),
                createSegment("Rodoanel Oeste", "Trecho Cotia Leste", 13.9, 18.0, VegetationClass.MODERATE, OperationalCriticality.MEDIUM, 18, 30, false, false, 3, 2, 35, 26, 70, 49, 36, "Moderate recurrence with medium roadside exposure."),
                createSegment("Rodoanel Oeste", "Trecho Carapicuiba", 18.1, 22.3, VegetationClass.CONTROLLED, OperationalCriticality.LOW, 12, 35, false, false, 2, 1, 22, 24, 65, 61, 18, "Under control but still monitored."),
                createSegment("Rodoanel Oeste", "Trecho Itapevi Operacional", 22.4, 26.9, VegetationClass.MODERATE, OperationalCriticality.HIGH, 34, 26, true, true, 4, 4, 46, 28, 78, 73, 28, "Maintenance delays impact operations near junction."),
                createSegment("Rodoanel Oeste", "Trecho Jandira Perimetral", 27.0, 31.0, VegetationClass.CONTROLLED, OperationalCriticality.MEDIUM, 20, 32, false, false, 3, 2, 26, 25, 67, 84, 38, "Planned surveillance area."),
                createSegment("Rodoanel Oeste", "Trecho Alphaville Conector", 31.1, 35.4, VegetationClass.AGGRESSIVE, OperationalCriticality.HIGH, 41, 21, true, true, 6, 5, 63, 30, 82, 92, 20, "Critical corridor with sensitive margins and high recurrence.")
            );
            roadSegmentRepository.saveAll(segments);

            teamRepository.saveAll(List.of(
                createTeam("Equipe Alfa", 32, "Rodoanel Oeste"),
                createTeam("Equipe Bravo", 30, "Rodoanel Oeste"),
                createTeam("Equipe Charlie", 28, "Rodoanel Oeste"),
                createTeam("Equipe Delta", 24, "Rodoanel Oeste")
            ));

            for (RoadSegment segment : segments) {
                WeatherSnapshot snapshot = new WeatherSnapshot();
                snapshot.setRoadSegment(segment);
                snapshot.setSnapshotDate(LocalDate.now());
                snapshot.setRainfallMm(segment.getRecentRainfallMm());
                snapshot.setTemperatureCelsius(segment.getAverageTemperatureCelsius());
                snapshot.setHumidityPercent(segment.getHumidityPercent());
                weatherSnapshotRepository.save(snapshot);
                priorityAssessmentService.calculateAndStoreForSegment(segment);
            }
        };
    }

    private RoadSegment createSegment(
        String highway,
        String name,
        double kmStart,
        double kmEnd,
        VegetationClass vegetationClass,
        OperationalCriticality operationalCriticality,
        int daysAgo,
        int frequency,
        boolean sensitiveArea,
        boolean contractualPressure,
        int recurrenceIndex,
        int inspectorSignal,
        double rainfall,
        double temperature,
        double humidity,
        double mapX,
        double mapY,
        String notes
    ) {
        RoadSegment segment = new RoadSegment();
        segment.setHighway(highway);
        segment.setName(name);
        segment.setKmStart(kmStart);
        segment.setKmEnd(kmEnd);
        segment.setExtensionKm(Math.round((kmEnd - kmStart) * 10.0) / 10.0);
        segment.setVegetationClass(vegetationClass);
        segment.setOperationalCriticality(operationalCriticality);
        segment.setLastMowingDate(LocalDate.now().minusDays(daysAgo));
        segment.setHistoricalFrequencyDays(frequency);
        segment.setSensitiveArea(sensitiveArea);
        segment.setContractualPressure(contractualPressure);
        segment.setRecurrenceIndex(recurrenceIndex);
        segment.setInspectorSignal(inspectorSignal);
        segment.setRecentRainfallMm(rainfall);
        segment.setAverageTemperatureCelsius(temperature);
        segment.setHumidityPercent(humidity);
        segment.setMapX(mapX);
        segment.setMapY(mapY);
        segment.setNotes(notes);
        return segment;
    }

    private Team createTeam(String name, int hours, String baseHighway) {
        Team team = new Team();
        team.setName(name);
        team.setWeeklyCapacityHours(hours);
        team.setBaseHighway(baseHighway);
        return team;
    }
}
