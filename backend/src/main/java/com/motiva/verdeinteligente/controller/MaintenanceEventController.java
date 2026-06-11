package com.motiva.verdeinteligente.controller;

import com.motiva.verdeinteligente.dto.MaintenanceEventRequest;
import com.motiva.verdeinteligente.dto.MaintenanceEventResponse;
import com.motiva.verdeinteligente.service.MaintenanceEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maintenance-events")
public class MaintenanceEventController {

    private final MaintenanceEventService maintenanceEventService;

    public MaintenanceEventController(MaintenanceEventService maintenanceEventService) {
        this.maintenanceEventService = maintenanceEventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MaintenanceEventResponse create(@Valid @RequestBody MaintenanceEventRequest request) {
        return maintenanceEventService.create(request);
    }
}
