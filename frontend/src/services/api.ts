import type {
  CriticalSegmentReportItem,
  DashboardAssumptions,
  DashboardOverview,
  EfficiencySummary,
  GrassGrowthPrediction,
  GrassGrowthRankingItem,
  LiveWeather,
  OperationalAlert,
  PriorityAssessment,
  PriorityDistributionItem,
  SegmentDetail,
  SegmentSummary,
  WeatherRefresh,
  WeeklyPlan,
} from "../types";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080/api";

async function apiFetch<T>(path: string, init?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(init?.headers ?? {}),
    },
    ...init,
  });

  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `Request failed with status ${response.status}`);
  }

  return response.json() as Promise<T>;
}

export const api = {
  getDashboardAssumptions: () => apiFetch<DashboardAssumptions>("/dashboard/assumptions"),
  getDashboardOverview: () => apiFetch<DashboardOverview>("/dashboard/overview"),
  getSegments: () => apiFetch<SegmentSummary[]>("/segments"),
  getSegmentDetail: (id: string) => apiFetch<SegmentDetail>(`/segments/${id}`),
  getRanking: () => apiFetch<PriorityAssessment[]>("/priority-ranking"),
  getLiveWeather: (segmentId: string) => apiFetch<LiveWeather>(`/weather/live/${segmentId}`),
  refreshLiveWeather: (segmentId: string) =>
    apiFetch<LiveWeather>(`/weather/live/${segmentId}/refresh`, { method: "POST" }),
  refreshAllWeather: () => apiFetch<WeatherRefresh>("/weather/live/refresh-all", { method: "POST" }),
  getGrassGrowthPrediction: (segmentId: string) => apiFetch<GrassGrowthPrediction>(`/ml/grass-growth/${segmentId}`),
  getGrassGrowthRanking: () => apiFetch<GrassGrowthRankingItem[]>("/ml/grass-growth/ranking"),
  recalculatePriorities: (rainfallDeltaMm: number, inspectorSignalBoost: number) =>
    apiFetch<PriorityAssessment[]>("/priority-assessments/recalculate", {
      method: "POST",
      body: JSON.stringify({ rainfallDeltaMm, inspectorSignalBoost }),
    }),
  generateWeeklyPlan: (startDate: string, crewCount: number, scenarioRainfallMm: number) =>
    apiFetch<WeeklyPlan>("/weekly-plans/generate", {
      method: "POST",
      body: JSON.stringify({ startDate, crewCount, scenarioRainfallMm }),
    }),
  getCriticalSegments: () => apiFetch<CriticalSegmentReportItem[]>("/reports/critical-segments"),
  getPriorityDistribution: () => apiFetch<PriorityDistributionItem[]>("/reports/priority-distribution"),
  getOperationalAlerts: () => apiFetch<OperationalAlert[]>("/reports/operational-alerts"),
  getEfficiencySummary: () => apiFetch<EfficiencySummary>("/reports/efficiency-summary"),
};
