import type { OperationalAlert } from "../types";

interface CriticalAlertsPanelProps {
  alerts: OperationalAlert[];
}

export function CriticalAlertsPanel({ alerts }: CriticalAlertsPanelProps) {
  return (
    <section className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Alertas operacionais</p>
          <h2>O que precisa ser dito na banca</h2>
        </div>
      </div>
      <div className="alerts-list">
        {alerts.map((alert) => (
          <article key={alert.title} className={`alert-card severity-${alert.severity.toLowerCase()}`}>
            <strong>{alert.title}</strong>
            <span>{alert.severity}</span>
            <p>{alert.summary}</p>
          </article>
        ))}
      </div>
    </section>
  );
}
