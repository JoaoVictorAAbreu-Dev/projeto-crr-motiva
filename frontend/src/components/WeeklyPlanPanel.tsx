import type { WeeklyPlan } from "../types";

interface WeeklyPlanPanelProps {
  plan: WeeklyPlan | null;
}

export function WeeklyPlanPanel({ plan }: WeeklyPlanPanelProps) {
  return (
    <section className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Planejamento semanal</p>
          <h2>Distribuicao de equipes</h2>
        </div>
      </div>

      {!plan ? (
        <p className="empty-state">Gere um plano semanal para visualizar a distribuicao por equipe e janela sugerida.</p>
      ) : (
        <>
          <div className="plan-summary">
            <span>Inicio: {plan.startDate}</span>
            <span>Equipes: {plan.requestedCrewCount}</span>
            <span>Horas alocadas: {plan.totalAssignedHours}</span>
            <span>Pendencias: {plan.pendingSegments}</span>
          </div>
          <div className="plan-list">
            {plan.items.map((item) => (
              <article key={`${item.executionOrder}-${item.segmentName}`} className="plan-card">
                <div className="plan-order">#{item.executionOrder}</div>
                <div>
                  <h3>{item.segmentName}</h3>
                  <p>
                    {item.teamName} • {item.recommendedWindow} • {item.estimatedHours}h
                  </p>
                  <small>{item.justification}</small>
                </div>
                <div className={`priority-badge ${item.priorityLevel.toLowerCase()}`}>{item.priorityLevel}</div>
              </article>
            ))}
          </div>
        </>
      )}
    </section>
  );
}
