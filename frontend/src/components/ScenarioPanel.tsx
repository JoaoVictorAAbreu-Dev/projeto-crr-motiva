import { useState } from "react";

interface ScenarioPanelProps {
  onRecalculate: (rainfall: number, inspectorBoost: number) => Promise<void>;
  onGeneratePlan: (crewCount: number, rainfall: number, startDate: string) => Promise<void>;
}

export function ScenarioPanel({ onRecalculate, onGeneratePlan }: ScenarioPanelProps) {
  const [rainfall, setRainfall] = useState(18);
  const [inspectorBoost, setInspectorBoost] = useState(2);
  const [crewCount, setCrewCount] = useState(3);
  const [startDate, setStartDate] = useState(new Date().toISOString().slice(0, 10));

  return (
    <section className="panel scenario-panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Simulador operacional</p>
          <h2>Teste cenários de clima e capacidade</h2>
        </div>
        <p className="panel-copy">
          Recalcule prioridades e gere o plano semanal com as restrições de campo.
        </p>
      </div>

      <div className="scenario-grid">
        <label>
          Chuva extra prevista (mm)
          <input type="number" min={0} max={120} value={rainfall} onChange={(event) => setRainfall(Number(event.target.value))} />
        </label>
        <label>
          Sinal do fiscal
          <input type="number" min={0} max={5} value={inspectorBoost} onChange={(event) => setInspectorBoost(Number(event.target.value))} />
        </label>
        <label>
          Equipes disponíveis
          <input type="number" min={1} max={10} value={crewCount} onChange={(event) => setCrewCount(Number(event.target.value))} />
        </label>
        <label>
          Início da semana
          <input type="date" value={startDate} onChange={(event) => setStartDate(event.target.value)} />
        </label>
      </div>

      <div className="scenario-actions">
        <button className="button secondary" onClick={() => onRecalculate(rainfall, inspectorBoost)}>
          Recalcular criticidade
        </button>
        <button className="button primary" onClick={() => onGeneratePlan(crewCount, rainfall, startDate)}>
          Gerar plano semanal
        </button>
      </div>
    </section>
  );
}
