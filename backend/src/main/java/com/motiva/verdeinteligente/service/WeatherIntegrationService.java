package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.dto.LiveWeatherResponse;
import com.motiva.verdeinteligente.dto.WeatherRefreshResponse;
import com.motiva.verdeinteligente.exception.ResourceNotFoundException;
import com.motiva.verdeinteligente.model.RoadSegment;
import com.motiva.verdeinteligente.model.WeatherSnapshot;
import com.motiva.verdeinteligente.repository.RoadSegmentRepository;
import com.motiva.verdeinteligente.repository.WeatherSnapshotRepository;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
public class WeatherIntegrationService {

    private final RoadSegmentRepository roadSegmentRepository;
    private final WeatherSnapshotRepository weatherSnapshotRepository;
    private final PriorityAssessmentService priorityAssessmentService;
    private final RestClient restClient;

    @Value("${app.weather.api-key:}")
    private String apiKey;

    @Value("${app.weather.base-url}")
    private String baseUrl;

    public WeatherIntegrationService(
        RoadSegmentRepository roadSegmentRepository,
        WeatherSnapshotRepository weatherSnapshotRepository,
        PriorityAssessmentService priorityAssessmentService
    ) {
        this.roadSegmentRepository = roadSegmentRepository;
        this.weatherSnapshotRepository = weatherSnapshotRepository;
        this.priorityAssessmentService = priorityAssessmentService;
        this.restClient = RestClient.builder().build();
    }

    public LiveWeatherResponse currentForSegment(UUID segmentId) {
        RoadSegment segment = roadSegmentRepository.findByPublicId(segmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Segment not found: " + segmentId));

        WeatherSnapshot snapshot = weatherSnapshotRepository.findAll().stream()
            .filter(item -> item.getRoadSegment().getId().equals(segment.getId()))
            .max(Comparator.comparing(WeatherSnapshot::getSnapshotDate))
            .orElseGet(() -> snapshotFromSegment(segment, false));

        return toResponse(segment, snapshot, false, "segment-cache");
    }

    @Transactional
    public LiveWeatherResponse refreshForSegment(UUID segmentId) {
        RoadSegment segment = roadSegmentRepository.findByPublicId(segmentId)
            .orElseThrow(() -> new ResourceNotFoundException("Segment not found: " + segmentId));

        WeatherSnapshot snapshot = fetchAndPersist(segment);
        priorityAssessmentService.calculateAndStoreForSegment(segment);
        return toResponse(segment, snapshot, hasLiveProvider(), hasLiveProvider() ? "openweather" : "fallback-simulation");
    }

    @Transactional
    public WeatherRefreshResponse refreshAllSegments() {
        List<LiveWeatherResponse> snapshots = roadSegmentRepository.findAll().stream()
            .map(segment -> {
                WeatherSnapshot snapshot = fetchAndPersist(segment);
                priorityAssessmentService.calculateAndStoreForSegment(segment);
                return toResponse(segment, snapshot, hasLiveProvider(), hasLiveProvider() ? "openweather" : "fallback-simulation");
            })
            .toList();

        return new WeatherRefreshResponse(snapshots.size(), hasLiveProvider(), snapshots);
    }

    private WeatherSnapshot fetchAndPersist(RoadSegment segment) {
        WeatherSnapshot snapshot = hasLiveProvider() ? fetchLiveWeather(segment) : snapshotFromSegment(segment, true);

        segment.setRecentRainfallMm(snapshot.getRainfallMm());
        segment.setAverageTemperatureCelsius(snapshot.getTemperatureCelsius());
        segment.setHumidityPercent(snapshot.getHumidityPercent());
        roadSegmentRepository.save(segment);
        return weatherSnapshotRepository.save(snapshot);
    }

    @SuppressWarnings("unchecked")
    private WeatherSnapshot fetchLiveWeather(RoadSegment segment) {
        Map<String, Object> payload = restClient.get()
            .uri(baseUrl + "/weather?lat={lat}&lon={lon}&appid={appid}&units=metric", segment.getLatitude(), segment.getLongitude(), apiKey)
            .retrieve()
            .body(Map.class);

        Map<String, Object> main = (Map<String, Object>) payload.get("main");
        Map<String, Object> rain = payload.containsKey("rain") ? (Map<String, Object>) payload.get("rain") : Map.of();

        WeatherSnapshot snapshot = new WeatherSnapshot();
        snapshot.setRoadSegment(segment);
        snapshot.setSnapshotDate(LocalDate.now());
        snapshot.setTemperatureCelsius(readNumber(main.get("temp")));
        snapshot.setHumidityPercent(readNumber(main.get("humidity")));
        snapshot.setRainfallMm(readNumber(rain.getOrDefault("1h", 0.0)));
        return snapshot;
    }

    private WeatherSnapshot snapshotFromSegment(RoadSegment segment, boolean dynamic) {
        WeatherSnapshot snapshot = new WeatherSnapshot();
        snapshot.setRoadSegment(segment);
        snapshot.setSnapshotDate(LocalDate.now());
        snapshot.setRainfallMm(dynamic ? Math.min(90, segment.getRecentRainfallMm() + 6) : segment.getRecentRainfallMm());
        snapshot.setTemperatureCelsius(dynamic ? segment.getAverageTemperatureCelsius() + 0.8 : segment.getAverageTemperatureCelsius());
        snapshot.setHumidityPercent(dynamic ? Math.min(95, segment.getHumidityPercent() + 3) : segment.getHumidityPercent());
        return snapshot;
    }

    private LiveWeatherResponse toResponse(RoadSegment segment, WeatherSnapshot snapshot, boolean liveData, String source) {
        return new LiveWeatherResponse(
            segment.getPublicId(),
            segment.getName(),
            liveData,
            snapshot.getSnapshotDate(),
            snapshot.getRainfallMm(),
            snapshot.getTemperatureCelsius(),
            snapshot.getHumidityPercent(),
            source
        );
    }

    private boolean hasLiveProvider() {
        return apiKey != null && !apiKey.isBlank();
    }

    private double readNumber(Object value) {
        return value instanceof Number number ? number.doubleValue() : 0.0;
    }
}
