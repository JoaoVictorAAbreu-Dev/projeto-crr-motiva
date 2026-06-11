package com.motiva.verdeinteligente.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class RoadSegment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID publicId;

    @Column(nullable = false)
    private String highway;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double kmStart;

    @Column(nullable = false)
    private double kmEnd;

    @Column(nullable = false)
    private double extensionKm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VegetationClass vegetationClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationalCriticality operationalCriticality;

    @Column(nullable = false)
    private LocalDate lastMowingDate;

    @Column(nullable = false)
    private int historicalFrequencyDays;

    @Column(nullable = false)
    private boolean sensitiveArea;

    @Column(nullable = false)
    private boolean contractualPressure;

    @Column(nullable = false)
    private int recurrenceIndex;

    @Column(nullable = false)
    private int inspectorSignal;

    @Column(nullable = false)
    private double recentRainfallMm;

    @Column(nullable = false)
    private double averageTemperatureCelsius;

    @Column(nullable = false)
    private double humidityPercent;

    @Column(nullable = false)
    private double mapX;

    @Column(nullable = false)
    private double mapY;

    @Column(length = 500)
    private String notes;

    @PrePersist
    public void assignPublicId() {
        if (publicId == null) {
            publicId = UUID.randomUUID();
        }
    }

    public Long getId() {
        return id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getHighway() {
        return highway;
    }

    public void setHighway(String highway) {
        this.highway = highway;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getKmStart() {
        return kmStart;
    }

    public void setKmStart(double kmStart) {
        this.kmStart = kmStart;
    }

    public double getKmEnd() {
        return kmEnd;
    }

    public void setKmEnd(double kmEnd) {
        this.kmEnd = kmEnd;
    }

    public double getExtensionKm() {
        return extensionKm;
    }

    public void setExtensionKm(double extensionKm) {
        this.extensionKm = extensionKm;
    }

    public VegetationClass getVegetationClass() {
        return vegetationClass;
    }

    public void setVegetationClass(VegetationClass vegetationClass) {
        this.vegetationClass = vegetationClass;
    }

    public OperationalCriticality getOperationalCriticality() {
        return operationalCriticality;
    }

    public void setOperationalCriticality(OperationalCriticality operationalCriticality) {
        this.operationalCriticality = operationalCriticality;
    }

    public LocalDate getLastMowingDate() {
        return lastMowingDate;
    }

    public void setLastMowingDate(LocalDate lastMowingDate) {
        this.lastMowingDate = lastMowingDate;
    }

    public int getHistoricalFrequencyDays() {
        return historicalFrequencyDays;
    }

    public void setHistoricalFrequencyDays(int historicalFrequencyDays) {
        this.historicalFrequencyDays = historicalFrequencyDays;
    }

    public boolean isSensitiveArea() {
        return sensitiveArea;
    }

    public void setSensitiveArea(boolean sensitiveArea) {
        this.sensitiveArea = sensitiveArea;
    }

    public boolean isContractualPressure() {
        return contractualPressure;
    }

    public void setContractualPressure(boolean contractualPressure) {
        this.contractualPressure = contractualPressure;
    }

    public int getRecurrenceIndex() {
        return recurrenceIndex;
    }

    public void setRecurrenceIndex(int recurrenceIndex) {
        this.recurrenceIndex = recurrenceIndex;
    }

    public int getInspectorSignal() {
        return inspectorSignal;
    }

    public void setInspectorSignal(int inspectorSignal) {
        this.inspectorSignal = inspectorSignal;
    }

    public double getRecentRainfallMm() {
        return recentRainfallMm;
    }

    public void setRecentRainfallMm(double recentRainfallMm) {
        this.recentRainfallMm = recentRainfallMm;
    }

    public double getAverageTemperatureCelsius() {
        return averageTemperatureCelsius;
    }

    public void setAverageTemperatureCelsius(double averageTemperatureCelsius) {
        this.averageTemperatureCelsius = averageTemperatureCelsius;
    }

    public double getHumidityPercent() {
        return humidityPercent;
    }

    public void setHumidityPercent(double humidityPercent) {
        this.humidityPercent = humidityPercent;
    }

    public double getMapX() {
        return mapX;
    }

    public void setMapX(double mapX) {
        this.mapX = mapX;
    }

    public double getMapY() {
        return mapY;
    }

    public void setMapY(double mapY) {
        this.mapY = mapY;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
