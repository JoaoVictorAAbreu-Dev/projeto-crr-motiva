package com.motiva.verdeinteligente.repository;

import com.motiva.verdeinteligente.model.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {
}
