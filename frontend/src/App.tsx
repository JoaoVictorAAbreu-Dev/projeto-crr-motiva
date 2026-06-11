import { startTransition, useDeferredValue, useEffect, useState } from "react";
import { OperationalMap } from "./components/OperationalMap";
import { RankingTable } from "./components/RankingTable";
import { ScenarioPanel } from "./components/ScenarioPanel";
import { SegmentInspector } from "./components/SegmentInspector";
import { StatCard } from "./components/StatCard";
import { WeeklyPlanPanel } from "./components/WeeklyPlanPanel";
import { api } from "./services/api";
import type {
  DashboardOverview,
  EfficiencySummary,
  PriorityAssessment,
  SegmentDetail,
  SegmentSummary,
  WeeklyPlan,
} from "./types";

const currency = new Intl.NumberFormat("pt-BR", {
  style: "currency",
  currency: "BRL",
  maximumFractionDigits: 0,
});

export default function App() {
  const [dashboard, setDashboard] = useState<DashboardOverview | null>(null);
  const [efficiency, setEfficiency] = useState<EfficiencySummary | null>(null);
  const [segments, setSegments] = useState<SegmentSummary[]>([]);
  const deferredSegments = useDeferredValue(segments);
  const [ranking, setRanking] = useState<PriorityAssessment[]>([]);
  const [selectedSegmentId, setSelectedSegmentId] = useState<string | null>(null);
  const [selectedSegment, setSelectedSegment] = useState<SegmentDetail | null>(null);
  const [plan, setPlan] = useState<WeeklyPlan | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadInitialData() {
      try {
        const [dashboardData, efficiencyData, segmentsData, rankingData] = await Promise.all([
          api.getDashboardOverview(),
          api.getEfficiencySummary(),
          api.getSegments(),
          api.getRanking(),
        ]);

        startTransition(() => {
          setDashboard(dashboardData);
          setEfficiency(efficiencyData);
          setSegments(segmentsData);
          setRanking(rankingData);
          if (segmentsData[0]) {
            setSelectedSegmentId(segmentsData[0].id);
          }
        });
      } catch (loadError) {
        setError(loadError instanceof Error ? loadError.message : "Failed to load operational data.");
      } finally {
        setLoading(false);
      }
    }

    void loadInitialData();
  }, []);

  useEffect(() => {
    if (!selectedSegmentId) {
      return;
    }

    void api
      .getSegmentDetail(selectedSegmentId)
      .then((segment) => setSelectedSegment(segment))
      .catch((requestError) =>
        setError(requestError instanceof Error ? requestError.message : "Failed to load segment details."),
      );
  }, [selectedSegmentId]);

  async function refreshData() {
    const [dashboardData, efficiencyData, segmentsData, rankingData] = await Promise.all([
      api.getDashboardOverview(),
      api.getEfficiencySummary(),
      api.getSegments(),
      api.getRanking(),
    ]);

    startTransition(() => {
      setDashboard(dashboardData);
      setEfficiency(efficiencyData);
      setSegments(segmentsData);
      setRanking(rankingData);
    });
  }

  async function handleRecalculate(rainfall: number, inspectorBoost: number) {
    setError(null);
    await api.recalculatePriorities(rainfall, inspectorBoost);
    await refreshData();
    if (selectedSegmentId) {
      const detail = await api.getSegmentDetail(selectedSegmentId);
      setSelectedSegment(detail);
    }
  }

  async function handleGeneratePlan(crewCount: number, rainfall: number, startDate: string) {
    setError(null);
    const nextPlan = await api.generateWeeklyPlan(startDate, crewCount, rainfall);
    setPlan(nextPlan);
  }

  if (loading) {
    return <div className="app-shell loading-state">Carregando centro operacional...</div>;
  }

  return (
    <div className="app-shell">
      <header className="hero">
        <div>
          <p className="hero-kicker">Plataforma de priorizacao operacional explicavel</p>
          <h1>Motiva Verde Inteligente</h1>
          <p className="hero-copy">
            Plataforma para usar dados simulados consistentes, antecipar risco viario, reduzir rocadas
            desnecessarias e distribuir equipes com base em clima, seguranca e restricoes contratuais.
          </p>
        </div>
        <div className="hero-highlight">
          <span className="hero-chip">Entrada</span>
          <span className="hero-chip">Processamento</span>
          <span className="hero-chip">Saida</span>
        </div>
      </header>

      {error ? <div className="error-banner">{error}</div> : null}

      <section className="stats-grid">
        <StatCard label="Trechos monitorados" value={`${dashboard?.totalSegments ?? 0}`} hint="segmentos operacionais ativos" />
        <StatCard label="Criticidade alta" value={`${dashboard?.criticalSegments ?? 0}`} hint="demandam intervencao imediata" />
        <StatCard label="Media do IPI" value={`${dashboard?.averageScore ?? 0}`} hint="visao consolidada de risco" />
        <StatCard label="Economia estimada" value={currency.format(efficiency?.estimatedSavings ?? 0)} hint="comparacao com cronograma fixo" />
      </section>

      <main className="main-grid">
        <div className="left-column">
          <OperationalMap
            segments={deferredSegments}
            selectedId={selectedSegmentId}
            onSelect={setSelectedSegmentId}
          />
          <ScenarioPanel onRecalculate={handleRecalculate} onGeneratePlan={handleGeneratePlan} />
          <RankingTable ranking={ranking} />
        </div>

        <div className="right-column">
          <SegmentInspector segment={selectedSegment} />
          <WeeklyPlanPanel plan={plan} />
          <section className="panel report-panel">
            <div className="panel-header">
              <div>
                <p className="eyebrow">Indicadores executivos</p>
                <h2>Ganho operacional esperado</h2>
              </div>
            </div>
            <div className="report-grid">
              <article>
                <strong>{currency.format(efficiency?.estimatedWeeklyCostFixedSchedule ?? 0)}</strong>
                <span>Custo estimado com cronograma fixo</span>
              </article>
              <article>
                <strong>{currency.format(efficiency?.estimatedWeeklyCostSmartPlan ?? 0)}</strong>
                <span>Custo estimado com priorizacao inteligente</span>
              </article>
              <article>
                <strong>{dashboard?.availableTeams ?? 0}</strong>
                <span>Equipes disponiveis para a semana</span>
              </article>
            </div>
            <p className="report-copy">{efficiency?.summary}</p>
          </section>
        </div>
      </main>
    </div>
  );
}
