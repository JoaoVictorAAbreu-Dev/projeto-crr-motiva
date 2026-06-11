import { startTransition, useDeferredValue, useEffect, useState } from "react";
import { AssumptionsPanel } from "./components/AssumptionsPanel";
import { CriticalAlertsPanel } from "./components/CriticalAlertsPanel";
import { FilterToolbar } from "./components/FilterToolbar";
import { GrassGrowthPanel } from "./components/GrassGrowthPanel";
import { LoginPanel } from "./components/LoginPanel";
import { OperationalMap } from "./components/OperationalMap";
import { PriorityDistributionPanel } from "./components/PriorityDistributionPanel";
import { RankingTable } from "./components/RankingTable";
import { ScenarioPanel } from "./components/ScenarioPanel";
import { SegmentInspector } from "./components/SegmentInspector";
import { StatCard } from "./components/StatCard";
import { WeatherPanel } from "./components/WeatherPanel";
import { WeeklyPlanPanel } from "./components/WeeklyPlanPanel";
import { api, authSession } from "./services/api";
import type {
  AuthenticatedUser,
  DashboardAssumptions,
  DashboardOverview,
  EfficiencySummary,
  GrassGrowthPrediction,
  GrassGrowthRankingItem,
  LiveWeather,
  OperationalAlert,
  PriorityAssessment,
  PriorityDistributionItem,
  PriorityLevel,
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
  const [currentUser, setCurrentUser] = useState<AuthenticatedUser | null>(null);
  const [dashboard, setDashboard] = useState<DashboardOverview | null>(null);
  const [assumptions, setAssumptions] = useState<DashboardAssumptions | null>(null);
  const [efficiency, setEfficiency] = useState<EfficiencySummary | null>(null);
  const [distribution, setDistribution] = useState<PriorityDistributionItem[]>([]);
  const [alerts, setAlerts] = useState<OperationalAlert[]>([]);
  const [segments, setSegments] = useState<SegmentSummary[]>([]);
  const deferredSegments = useDeferredValue(segments);
  const [ranking, setRanking] = useState<PriorityAssessment[]>([]);
  const [selectedSegmentId, setSelectedSegmentId] = useState<string | null>(null);
  const [selectedSegment, setSelectedSegment] = useState<SegmentDetail | null>(null);
  const [liveWeather, setLiveWeather] = useState<LiveWeather | null>(null);
  const [grassPrediction, setGrassPrediction] = useState<GrassGrowthPrediction | null>(null);
  const [grassRanking, setGrassRanking] = useState<GrassGrowthRankingItem[]>([]);
  const [plan, setPlan] = useState<WeeklyPlan | null>(null);
  const [selectedHighway, setSelectedHighway] = useState("ALL");
  const [selectedPriority, setSelectedPriority] = useState<PriorityLevel | "ALL">("ALL");
  const [contractOnly, setContractOnly] = useState(false);
  const [sensitiveOnly, setSensitiveOnly] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [authLoading, setAuthLoading] = useState(true);

  useEffect(() => {
    async function bootstrap() {
      const token = authSession.getToken();
      if (!token) {
        setLoading(false);
        setAuthLoading(false);
        return;
      }

      try {
        const user = await api.getCurrentUser();
        setCurrentUser(user);
        await refreshData();
      } catch {
        authSession.clear();
        setCurrentUser(null);
      } finally {
        setLoading(false);
        setAuthLoading(false);
      }
    }

    void bootstrap();
  }, []);

  useEffect(() => {
    if (!selectedSegmentId || !currentUser) {
      setSelectedSegment(null);
      setLiveWeather(null);
      setGrassPrediction(null);
      return;
    }

    const segmentId = selectedSegmentId;

    async function loadSegmentInsights() {
      try {
        const [segment, weather, prediction] = await Promise.all([
          api.getSegmentDetail(segmentId),
          api.getLiveWeather(segmentId),
          api.getGrassGrowthPrediction(segmentId),
        ]);

        startTransition(() => {
          setSelectedSegment(segment);
          setLiveWeather(weather);
          setGrassPrediction(prediction);
        });
      } catch (requestError) {
        setError(requestError instanceof Error ? requestError.message : "Failed to load segment details.");
      }
    }

    void loadSegmentInsights();
  }, [currentUser, selectedSegmentId]);

  async function refreshData() {
    const [
      dashboardData,
      assumptionsData,
      efficiencyData,
      distributionData,
      alertsData,
      segmentsData,
      rankingData,
      grassRankingData,
    ] = await Promise.all([
      api.getDashboardOverview(),
      api.getDashboardAssumptions(),
      api.getEfficiencySummary(),
      api.getPriorityDistribution(),
      api.getOperationalAlerts(),
      api.getSegments(),
      api.getRanking(),
      api.getGrassGrowthRanking(),
    ]);

    startTransition(() => {
      setDashboard(dashboardData);
      setAssumptions(assumptionsData);
      setEfficiency(efficiencyData);
      setDistribution(distributionData);
      setAlerts(alertsData);
      setSegments(segmentsData);
      setRanking(rankingData);
      setGrassRanking(grassRankingData);
      if (!selectedSegmentId && segmentsData[0]) {
        setSelectedSegmentId(segmentsData[0].id);
      }
    });
  }

  async function handleLogin(username: string, password: string) {
    setError(null);
    setAuthLoading(true);
    try {
      const auth = await api.login(username, password);
      authSession.setToken(auth.accessToken);
      setCurrentUser({
        username: auth.username,
        fullName: auth.fullName,
        role: auth.role,
      });
      await refreshData();
    } catch (requestError) {
      setError(requestError instanceof Error ? requestError.message : "Falha ao autenticar.");
      authSession.clear();
      setCurrentUser(null);
    } finally {
      setLoading(false);
      setAuthLoading(false);
    }
  }

  function handleLogout() {
    authSession.clear();
    setCurrentUser(null);
    setDashboard(null);
    setAssumptions(null);
    setEfficiency(null);
    setDistribution([]);
    setAlerts([]);
    setSegments([]);
    setRanking([]);
    setSelectedSegmentId(null);
    setSelectedSegment(null);
    setLiveWeather(null);
    setGrassPrediction(null);
    setGrassRanking([]);
    setPlan(null);
    setError(null);
  }

  async function handleRecalculate(rainfall: number, inspectorBoost: number) {
    setError(null);
    await api.recalculatePriorities(rainfall, inspectorBoost);
    await refreshData();
    if (selectedSegmentId) {
      const segmentId = selectedSegmentId;
      const [detail, weather, prediction] = await Promise.all([
        api.getSegmentDetail(segmentId),
        api.getLiveWeather(segmentId),
        api.getGrassGrowthPrediction(segmentId),
      ]);
      setSelectedSegment(detail);
      setLiveWeather(weather);
      setGrassPrediction(prediction);
    }
  }

  async function handleGeneratePlan(crewCount: number, rainfall: number, startDate: string) {
    setError(null);
    const nextPlan = await api.generateWeeklyPlan(startDate, crewCount, rainfall);
    setPlan(nextPlan);
  }

  async function handleRefreshWeather() {
    if (!selectedSegmentId) {
      return;
    }

    const segmentId = selectedSegmentId;
    setError(null);
    const weather = await api.refreshLiveWeather(segmentId);
    const [detail, prediction] = await Promise.all([
      api.getSegmentDetail(segmentId),
      api.getGrassGrowthPrediction(segmentId),
    ]);
    setLiveWeather(weather);
    setSelectedSegment(detail);
    setGrassPrediction(prediction);
    await refreshData();
  }

  if (authLoading) {
    return <div className="app-shell loading-state">Validando sessao operacional...</div>;
  }

  if (!currentUser) {
    return (
      <div className="app-shell">
        <LoginPanel error={error} onLogin={handleLogin} />
      </div>
    );
  }

  if (loading) {
    return <div className="app-shell loading-state">Carregando centro operacional...</div>;
  }

  const highways = Array.from(new Set(segments.map((segment) => segment.highway))).sort();
  const filteredSegments = deferredSegments.filter((segment) => {
    if (selectedHighway !== "ALL" && segment.highway !== selectedHighway) {
      return false;
    }
    if (selectedPriority !== "ALL" && segment.priorityLevel !== selectedPriority) {
      return false;
    }
    if (contractOnly && !segment.contractualPressure) {
      return false;
    }
    if (sensitiveOnly && !segment.sensitiveArea) {
      return false;
    }
    return true;
  });
  const filteredIds = new Set(filteredSegments.map((segment) => segment.id));
  const filteredRanking = ranking.filter((item) => filteredIds.has(item.segmentId));

  return (
    <div className="app-shell">
      <header className="hero">
        <div>
          <p className="hero-kicker">Plataforma de priorizacao operacional explicavel</p>
          <h1>GreenOps Control Center</h1>
          <p className="hero-copy">
            Plataforma para usar dados simulados consistentes, antecipar risco viario, reduzir rocadas
            desnecessarias e distribuir equipes com base em clima, seguranca e restricoes contratuais.
          </p>
        </div>
        <div className="hero-highlight">
          <span className="hero-chip">{currentUser.fullName}</span>
          <span className="hero-chip">{currentUser.role}</span>
          <span className="hero-chip">Entrada</span>
          <span className="hero-chip">Processamento</span>
          <span className="hero-chip">Saida</span>
          <button className="button secondary" onClick={handleLogout}>
            Sair
          </button>
        </div>
      </header>

      {error ? <div className="error-banner">{error}</div> : null}

      <section className="stats-grid">
        <StatCard label="Trechos monitorados" value={`${dashboard?.totalSegments ?? 0}`} hint="segmentos operacionais ativos" />
        <StatCard label="Criticidade alta" value={`${dashboard?.criticalSegments ?? 0}`} hint="demandam intervencao imediata" />
        <StatCard label="Media do IPI" value={`${dashboard?.averageScore ?? 0}`} hint="visao consolidada de risco" />
        <StatCard label="Economia estimada" value={currency.format(efficiency?.estimatedSavings ?? 0)} hint="comparacao com cronograma fixo" />
      </section>

      <section className="stats-grid compact-grid">
        <StatCard label="Risco contratual" value={`${dashboard?.segmentsAtContractRisk ?? 0}`} hint="trechos com obrigacao contratual sensivel" />
        <StatCard label="Areas sensiveis" value={`${dashboard?.segmentsInSensitiveAreas ?? 0}`} hint="locais com visibilidade e infraestrutura sensivel" />
        <StatCard label="Ciclos evitados" value={`${efficiency?.projectedAnnualCyclesAvoided ?? 0}`} hint="projecao anual de ciclos evitados" />
        <StatCard label="Uso sugerido" value={`${dashboard?.recommendedCrewUtilization ?? 0}%`} hint="aproveitamento recomendado da capacidade das equipes" />
      </section>

      <FilterToolbar
        highways={highways}
        selectedHighway={selectedHighway}
        selectedPriority={selectedPriority}
        contractOnly={contractOnly}
        sensitiveOnly={sensitiveOnly}
        onHighwayChange={setSelectedHighway}
        onPriorityChange={setSelectedPriority}
        onContractOnlyChange={setContractOnly}
        onSensitiveOnlyChange={setSensitiveOnly}
      />

      <main className="main-grid">
        <div className="left-column">
          <OperationalMap segments={filteredSegments} selectedId={selectedSegmentId} onSelect={setSelectedSegmentId} />
          <ScenarioPanel onRecalculate={handleRecalculate} onGeneratePlan={handleGeneratePlan} />
          <PriorityDistributionPanel distribution={distribution} />
          <RankingTable ranking={filteredRanking} onSelect={setSelectedSegmentId} />
        </div>

        <div className="right-column">
          <SegmentInspector segment={selectedSegment} />
          <WeatherPanel weather={liveWeather} onRefresh={handleRefreshWeather} />
          <GrassGrowthPanel prediction={grassPrediction} ranking={grassRanking.slice(0, 4)} />
          <CriticalAlertsPanel alerts={alerts} />
          <WeeklyPlanPanel plan={plan} />
          <AssumptionsPanel assumptions={assumptions} />
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
              <article>
                <strong>{efficiency?.mediumPrioritySegments ?? 0}</strong>
                <span>Trechos em observacao ativa</span>
              </article>
            </div>
            <div className="focus-strip">
              <span>{dashboard?.currentScenarioLabel}</span>
              <span>{efficiency?.operationalFocus}</span>
            </div>
            <p className="report-copy">{efficiency?.summary}</p>
          </section>
        </div>
      </main>
    </div>
  );
}
