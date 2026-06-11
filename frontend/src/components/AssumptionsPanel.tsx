import type { DashboardAssumptions } from "../types";

interface AssumptionsPanelProps {
  assumptions: DashboardAssumptions | null;
}

export function AssumptionsPanel({ assumptions }: AssumptionsPanelProps) {
  if (!assumptions) {
    return null;
  }

  return (
    <section className="panel assumptions-panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Premissas do MVP</p>
          <h2>Como defender a solucao na banca</h2>
        </div>
      </div>

      <div className="assumptions-grid">
        <article>
          <strong>Dados</strong>
          <p>{assumptions.dataPolicy}</p>
        </article>
        <article>
          <strong>Decisao</strong>
          <p>{assumptions.decisionPolicy}</p>
        </article>
        <article>
          <strong>Seguranca</strong>
          <p>{assumptions.safetyPolicy}</p>
        </article>
        <article>
          <strong>Privacidade</strong>
          <p>{assumptions.privacyPolicy}</p>
        </article>
      </div>

      <div className="assumptions-list-grid">
        <div>
          <strong>Referencias de custo</strong>
          <ul className="reason-list">
            {assumptions.costReferences.map((item) => (
              <li key={item}>{item}</li>
            ))}
          </ul>
        </div>
        <div>
          <strong>Referencias operacionais</strong>
          <ul className="reason-list">
            {assumptions.operationalReferences.map((item) => (
              <li key={item}>{item}</li>
            ))}
          </ul>
        </div>
      </div>
    </section>
  );
}
