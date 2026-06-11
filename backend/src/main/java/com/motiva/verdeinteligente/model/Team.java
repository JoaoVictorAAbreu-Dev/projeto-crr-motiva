package com.motiva.verdeinteligente.model;

import jakarta.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int weeklyCapacityHours;

    @Column(nullable = false)
    private String baseHighway;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeeklyCapacityHours() {
        return weeklyCapacityHours;
    }

    public void setWeeklyCapacityHours(int weeklyCapacityHours) {
        this.weeklyCapacityHours = weeklyCapacityHours;
    }

    public String getBaseHighway() {
        return baseHighway;
    }

    public void setBaseHighway(String baseHighway) {
        this.baseHighway = baseHighway;
    }
}
