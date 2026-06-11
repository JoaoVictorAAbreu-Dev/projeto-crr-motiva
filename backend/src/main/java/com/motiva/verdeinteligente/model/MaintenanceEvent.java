package com.motiva.verdeinteligente.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class MaintenanceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private RoadSegment roadSegment;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double costEstimate;

    @Column(nullable = false)
    private String crewName;

    @Column(length = 500)
    private String notes;

    public Long getId() {
        return id;
    }

    public RoadSegment getRoadSegment() {
        return roadSegment;
    }

    public void setRoadSegment(RoadSegment roadSegment) {
        this.roadSegment = roadSegment;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getCostEstimate() {
        return costEstimate;
    }

    public void setCostEstimate(double costEstimate) {
        this.costEstimate = costEstimate;
    }

    public String getCrewName() {
        return crewName;
    }

    public void setCrewName(String crewName) {
        this.crewName = crewName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
