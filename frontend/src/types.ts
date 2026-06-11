export type PriorityLevel = "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";

export interface SegmentSummary {
  id: string;
  highway: string;
  name: string;
  kmStart: number;
  kmEnd: number;
  extensionKm: number;
  vegetationClass: string;
  operationalCriticality: string;
  lastMowingDate: string;
  historicalFrequencyDays: number;
  mapX: number;
  mapY: number;
  score: number;
  priorityLevel: PriorityLevel;
  reasons: string;
}

export interface SegmentDetail {
  id: string;
  highway: string;
  name: string;
  kmStart: number;
  kmEnd: number;
  extensionKm: number;
  vegetationClass: string;
  operationalCriticality: string;
  lastMowingDate: string;
  historicalFrequencyDays: number;
  mapX: number;
  mapY: number;
  score: number;
  priorityLevel: PriorityLevel;
  sensitiveArea: boolean;
  contractualPressure: boolean;
  recurrenceIndex: number;
  inspectorSignal: number;
  recentRainfallMm: number;
  averageTemperatureCelsius: number;
  humidityPercent: number;
  notes: string;
  reasons: string[];
}

export interface PriorityAssessment {
  segmentId: string;
  segmentName: string;
  score: number;
  priorityLevel: PriorityLevel;
  reasons: string[];
}

export interface CriticalSegmentReportItem {
  segmentName: string;
  highway: string;
  score: number;
  priorityLevel: PriorityLevel;
  reasons: string;
}

export interface EfficiencySummary {
  criticalSegments: number;
  highPrioritySegments: number;
  estimatedWeeklyCostFixedSchedule: number;
  estimatedWeeklyCostSmartPlan: number;
  estimatedSavings: number;
  summary: string;
}

export interface DashboardOverview {
  totalSegments: number;
  criticalSegments: number;
  highPrioritySegments: number;
  availableTeams: number;
  averageScore: number;
  estimatedSavings: number;
  topCriticalSegments: CriticalSegmentReportItem[];
}

export interface WeeklyPlanItem {
  executionOrder: number;
  teamName: string;
  segmentName: string;
  highway: string;
  estimatedHours: number;
  recommendedWindow: string;
  score: number;
  priorityLevel: PriorityLevel;
  justification: string;
}

export interface WeeklyPlan {
  id: number;
  startDate: string;
  requestedCrewCount: number;
  scenarioRainfallMm: number;
  totalAssignedHours: number;
  pendingSegments: number;
  items: WeeklyPlanItem[];
}
