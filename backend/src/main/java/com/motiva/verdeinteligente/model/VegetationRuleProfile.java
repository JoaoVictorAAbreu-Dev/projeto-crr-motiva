package com.motiva.verdeinteligente.model;

import jakarta.persistence.*;

@Entity
public class VegetationRuleProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private VegetationClass vegetationClass;

    @Column(nullable = false)
    private double growthWeight;

    @Column(nullable = false)
    private int idealMaintenanceCycleDays;

    public Long getId() {
        return id;
    }

    public VegetationClass getVegetationClass() {
        return vegetationClass;
    }

    public void setVegetationClass(VegetationClass vegetationClass) {
        this.vegetationClass = vegetationClass;
    }

    public double getGrowthWeight() {
        return growthWeight;
    }

    public void setGrowthWeight(double growthWeight) {
        this.growthWeight = growthWeight;
    }

    public int getIdealMaintenanceCycleDays() {
        return idealMaintenanceCycleDays;
    }

    public void setIdealMaintenanceCycleDays(int idealMaintenanceCycleDays) {
        this.idealMaintenanceCycleDays = idealMaintenanceCycleDays;
    }
}
