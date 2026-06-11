package com.motiva.verdeinteligente.repository;

import com.motiva.verdeinteligente.model.PriorityAssessment;
import com.motiva.verdeinteligente.model.PriorityLevel;
import com.motiva.verdeinteligente.model.RoadSegment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorityAssessmentRepository extends JpaRepository<PriorityAssessment, Long> {

    Optional<PriorityAssessment> findByRoadSegment(RoadSegment roadSegment);

    List<PriorityAssessment> findAllByOrderByScoreDesc();

    List<PriorityAssessment> findByPriorityLevelOrderByScoreDesc(PriorityLevel priorityLevel);
}
