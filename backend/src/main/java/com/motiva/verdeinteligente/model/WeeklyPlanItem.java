package com.motiva.verdeinteligente.model;

import jakarta.persistence.*;

@Entity
public class WeeklyPlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private WeeklyPlan weeklyPlan;

    @ManyToOne(optional = false)
    private RoadSegment roadSegment;

    @ManyToOne(optional = false)
    private Team team;

    @Column(nullable = false)
    private int executionOrder;

    @Column(nullable = false)
    private int estimatedHours;

    @Column(nullable = false)
    private String recommendedWindow;

    @Column(nullable = false, length = 800)
    private String justification;

    public Long getId() {
        return id;
    }

    public WeeklyPlan getWeeklyPlan() {
        return weeklyPlan;
    }

    public void setWeeklyPlan(WeeklyPlan weeklyPlan) {
        this.weeklyPlan = weeklyPlan;
    }

    public RoadSegment getRoadSegment() {
        return roadSegment;
    }

    public void setRoadSegment(RoadSegment roadSegment) {
        this.roadSegment = roadSegment;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public int getExecutionOrder() {
        return executionOrder;
    }

    public void setExecutionOrder(int executionOrder) {
        this.executionOrder = executionOrder;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(int estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public String getRecommendedWindow() {
        return recommendedWindow;
    }

    public void setRecommendedWindow(String recommendedWindow) {
        this.recommendedWindow = recommendedWindow;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }
}
