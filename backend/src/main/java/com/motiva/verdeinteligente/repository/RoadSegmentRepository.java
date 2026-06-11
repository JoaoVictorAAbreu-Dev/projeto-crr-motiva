package com.motiva.verdeinteligente.repository;

import com.motiva.verdeinteligente.model.RoadSegment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadSegmentRepository extends JpaRepository<RoadSegment, Long> {

    Optional<RoadSegment> findByPublicId(UUID publicId);
}
