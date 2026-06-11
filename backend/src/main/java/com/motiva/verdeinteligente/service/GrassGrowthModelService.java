package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.model.RoadSegment;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GrassGrowthModelService {

    private static final String MODEL_VERSION = "sim-regression-v1";

    public GrassGrowthPredictionResult predict(RoadSegment segment) {
        double[] weights = trainSyntheticRegressionWeights();
        double daysSinceMowing = ChronoUnit.DAYS.between(segment.getLastMowingDate(), LocalDate.now());
        double vegetationFactor = switch (segment.getVegetationClass()) {
            case CONTROLLED -> 0.9;
            case MODERATE -> 1.25;
            case AGGRESSIVE -> 1.6;
        };

        double[] features = {
            1.0,
            daysSinceMowing,
            segment.getRecentRainfallMm(),
            segment.getAverageTemperatureCelsius(),
            segment.getHumidityPercent(),
            vegetationFactor,
            segment.getRecurrenceIndex(),
            segment.isSensitiveArea() ? 1.0 : 0.0,
            segment.isContractualPressure() ? 1.0 : 0.0
        };

        double predictedHeight = dot(weights, features);
        predictedHeight = Math.max(6.0, Math.min(95.0, round(predictedHeight)));
        double dailyGrowth = round(0.18 + (segment.getRecentRainfallMm() / 120.0) + ((segment.getAverageTemperatureCelsius() - 20.0) / 40.0) + (vegetationFactor * 0.35));
        int criticalHeight = switch (segment.getVegetationClass()) {
            case CONTROLLED -> 28;
            case MODERATE -> 24;
            case AGGRESSIVE -> 20;
        };
        int daysToCritical = dailyGrowth <= 0 ? 30 : Math.max(0, (int) Math.ceil((criticalHeight - predictedHeight) / dailyGrowth));

        List<String> drivers = new ArrayList<>();
        if (segment.getRecentRainfallMm() >= 40) {
            drivers.add("Recent rainfall increased predicted grass acceleration.");
        }
        if (segment.getAverageTemperatureCelsius() >= 28) {
            drivers.add("Temperature profile favors rapid regrowth.");
        }
        if (segment.getHumidityPercent() >= 75) {
            drivers.add("High humidity sustains vegetation expansion.");
        }
        if (segment.getRecurrenceIndex() >= 4) {
            drivers.add("Historical recurrence suggests frequent regrowth.");
        }
        if (segment.getVegetationClass().name().equals("AGGRESSIVE")) {
            drivers.add("Aggressive vegetation class raises expected height.");
        }
        if (drivers.isEmpty()) {
            drivers.add("Current conditions indicate moderate growth behavior.");
        }

        return new GrassGrowthPredictionResult(
            predictedHeight,
            dailyGrowth,
            daysToCritical,
            0.84,
            MODEL_VERSION,
            drivers
        );
    }

    private double[] trainSyntheticRegressionWeights() {
        List<double[]> rows = new ArrayList<>();
        List<Double> targets = new ArrayList<>();

        for (int days = 7; days <= 45; days += 4) {
            for (double rainfall : new double[] { 12, 28, 44, 60 }) {
                for (double temperature : new double[] { 23, 26, 29, 31 }) {
                    double humidity = rainfall >= 44 ? 82 : 68;
                    double vegetationFactor = rainfall >= 44 ? 1.5 : 1.1;
                    double recurrence = rainfall >= 44 ? 5 : 3;
                    double[] row = { 1.0, days, rainfall, temperature, humidity, vegetationFactor, recurrence, 0.0, 0.0 };
                    rows.add(row);
                    targets.add(6.0 + (days * 0.48) + (rainfall * 0.16) + ((temperature - 20) * 0.7) + ((humidity - 50) * 0.08) + (vegetationFactor * 7.5) + recurrence);
                }
            }
        }

        double[] weights = new double[9];
        double learningRate = 0.00002;
        for (int epoch = 0; epoch < 2500; epoch++) {
            for (int i = 0; i < rows.size(); i++) {
                double prediction = dot(weights, rows.get(i));
                double error = prediction - targets.get(i);
                for (int j = 0; j < weights.length; j++) {
                    weights[j] -= learningRate * error * rows.get(i)[j];
                }
            }
        }
        return weights;
    }

    private double dot(double[] weights, double[] features) {
        double result = 0;
        for (int i = 0; i < weights.length; i++) {
            result += weights[i] * features[i];
        }
        return result;
    }

    private double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
