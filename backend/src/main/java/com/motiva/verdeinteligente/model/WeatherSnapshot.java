package com.motiva.verdeinteligente.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class WeatherSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private RoadSegment roadSegment;

    @Column(nullable = false)
    private LocalDate snapshotDate;

    @Column(nullable = false)
    private double rainfallMm;

    @Column(nullable = false)
    private double temperatureCelsius;

    @Column(nullable = false)
    private double humidityPercent;

    public Long getId() {
        return id;
    }

    public RoadSegment getRoadSegment() {
        return roadSegment;
    }

    public void setRoadSegment(RoadSegment roadSegment) {
        this.roadSegment = roadSegment;
    }

    public LocalDate getSnapshotDate() {
        return snapshotDate;
    }

    public void setSnapshotDate(LocalDate snapshotDate) {
        this.snapshotDate = snapshotDate;
    }

    public double getRainfallMm() {
        return rainfallMm;
    }

    public void setRainfallMm(double rainfallMm) {
        this.rainfallMm = rainfallMm;
    }

    public double getTemperatureCelsius() {
        return temperatureCelsius;
    }

    public void setTemperatureCelsius(double temperatureCelsius) {
        this.temperatureCelsius = temperatureCelsius;
    }

    public double getHumidityPercent() {
        return humidityPercent;
    }

    public void setHumidityPercent(double humidityPercent) {
        this.humidityPercent = humidityPercent;
    }
}
