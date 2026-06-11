package com.motiva.verdeinteligente.controller;

import com.motiva.verdeinteligente.dto.LiveWeatherResponse;
import com.motiva.verdeinteligente.dto.WeatherRefreshResponse;
import com.motiva.verdeinteligente.service.WeatherIntegrationService;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather/live")
public class WeatherController {

    private final WeatherIntegrationService weatherIntegrationService;

    public WeatherController(WeatherIntegrationService weatherIntegrationService) {
        this.weatherIntegrationService = weatherIntegrationService;
    }

    @GetMapping("/{segmentId}")
    public LiveWeatherResponse current(@PathVariable UUID segmentId) {
        return weatherIntegrationService.currentForSegment(segmentId);
    }

    @PostMapping("/{segmentId}/refresh")
    public LiveWeatherResponse refresh(@PathVariable UUID segmentId) {
        return weatherIntegrationService.refreshForSegment(segmentId);
    }

    @PostMapping("/refresh-all")
    public WeatherRefreshResponse refreshAll() {
        return weatherIntegrationService.refreshAllSegments();
    }
}
