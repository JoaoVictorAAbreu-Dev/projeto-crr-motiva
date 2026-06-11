package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.dto.MaintenanceEventRequest;
import com.motiva.verdeinteligente.dto.MaintenanceEventResponse;
import com.motiva.verdeinteligente.model.MaintenanceEvent;
import com.motiva.verdeinteligente.model.RoadSegment;
import com.motiva.verdeinteligente.repository.MaintenanceEventRepository;
import com.motiva.verdeinteligente.repository.RoadSegmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaintenanceEventService {

    private final MaintenanceEventRepository maintenanceEventRepository;
    private final RoadSegmentService roadSegmentService;
    private final RoadSegmentRepository roadSegmentRepository;
    private final PriorityAssessmentService priorityAssessmentService;

    public MaintenanceEventService(
        MaintenanceEventRepository maintenanceEventRepository,
        RoadSegmentService roadSegmentService,
        RoadSegmentRepository roadSegmentRepository,
        PriorityAssessmentService priorityAssessmentService
    ) {
        this.maintenanceEventRepository = maintenanceEventRepository;
        this.roadSegmentService = roadSegmentService;
        this.roadSegmentRepository = roadSegmentRepository;
        this.priorityAssessmentService = priorityAssessmentService;
    }

    @Transactional
    public MaintenanceEventResponse create(MaintenanceEventRequest request) {
        RoadSegment segment = roadSegmentService.getEntity(request.segmentId());

        MaintenanceEvent event = new MaintenanceEvent();
        event.setRoadSegment(segment);
        event.setEventDate(request.eventDate());
        event.setType(request.type());
        event.setCostEstimate(request.costEstimate());
        event.setCrewName(request.crewName());
        event.setNotes(request.notes());
        maintenanceEventRepository.save(event);

        if (request.type().equalsIgnoreCase("MOWING")) {
            segment.setLastMowingDate(request.eventDate());
            segment.setInspectorSignal(Math.max(0, segment.getInspectorSignal() - 2));
            roadSegmentRepository.save(segment);
            priorityAssessmentService.calculateAndStoreForSegment(segment);
        }

        return new MaintenanceEventResponse(
            event.getId(),
            segment.getName(),
            event.getEventDate(),
            event.getType(),
            event.getCostEstimate(),
            event.getCrewName()
        );
    }
}
