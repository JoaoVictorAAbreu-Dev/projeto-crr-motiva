package com.motiva.verdeinteligente.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class WeeklyPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private int requestedCrewCount;

    @Column(nullable = false)
    private double scenarioRainfallMm;

    @Column(nullable = false)
    private int totalAssignedHours;

    @Column(nullable = false)
    private int pendingSegments;

    @OneToMany(mappedBy = "weeklyPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklyPlanItem> items = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getRequestedCrewCount() {
        return requestedCrewCount;
    }

    public void setRequestedCrewCount(int requestedCrewCount) {
        this.requestedCrewCount = requestedCrewCount;
    }

    public double getScenarioRainfallMm() {
        return scenarioRainfallMm;
    }

    public void setScenarioRainfallMm(double scenarioRainfallMm) {
        this.scenarioRainfallMm = scenarioRainfallMm;
    }

    public int getTotalAssignedHours() {
        return totalAssignedHours;
    }

    public void setTotalAssignedHours(int totalAssignedHours) {
        this.totalAssignedHours = totalAssignedHours;
    }

    public int getPendingSegments() {
        return pendingSegments;
    }

    public void setPendingSegments(int pendingSegments) {
        this.pendingSegments = pendingSegments;
    }

    public List<WeeklyPlanItem> getItems() {
        return items;
    }
}
