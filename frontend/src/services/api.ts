import type {
  AuthenticatedUser,
  AuthToken,
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
const ACCESS_TOKEN_KEY = "motiva.accessToken";

export const authSession = {
  getToken: () => localStorage.getItem(ACCESS_TOKEN_KEY),
  setToken: (token: string) => localStorage.setItem(ACCESS_TOKEN_KEY, token),
  clear: () => localStorage.removeItem(ACCESS_TOKEN_KEY),
};

async function apiFetch<T>(path: string, init?: RequestInit): Promise<T> {
  const token = authSession.getToken();
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(init?.headers ?? {}),
    },
    ...init,
  });

  if (!response.ok) {
    if (response.status === 401) {
      authSession.clear();
    }
    const rawMessage = await response.text();
    let message = rawMessage;
    try {
      const parsed = JSON.parse(rawMessage) as { message?: string };
      message = parsed.message ?? rawMessage;
    } catch {
      message = rawMessage;
    }
    throw new Error(message || (response.status === 401 ? "Sua sessao expirou. Faça login novamente." : `Request failed with status ${response.status}`));
  }

  return response.json() as Promise<T>;
}

export const api = {
  login: (username: string, password: string) =>
    apiFetch<AuthToken>("/auth/login", {
      method: "POST",
      body: JSON.stringify({ username, password }),
    }),
  getCurrentUser: () => apiFetch<AuthenticatedUser>("/auth/me"),
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
