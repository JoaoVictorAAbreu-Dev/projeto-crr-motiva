package com.motiva.verdeinteligente.repository;

import com.motiva.verdeinteligente.model.MaintenanceEvent;
import com.motiva.verdeinteligente.model.RoadSegment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceEventRepository extends JpaRepository<MaintenanceEvent, Long> {

    List<MaintenanceEvent> findByRoadSegmentOrderByEventDateDesc(RoadSegment roadSegment);
}
