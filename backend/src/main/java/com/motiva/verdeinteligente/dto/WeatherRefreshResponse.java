package com.motiva.verdeinteligente.dto;

import java.util.List;

public record WeatherRefreshResponse(
    int refreshedSegments,
    boolean liveProviderUsed,
    List<LiveWeatherResponse> snapshots
) {
}
