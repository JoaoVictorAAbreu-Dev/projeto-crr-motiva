import type { PriorityDistributionItem } from "../types";

interface PriorityDistributionPanelProps {
  distribution: PriorityDistributionItem[];
}

export function PriorityDistributionPanel({ distribution }: PriorityDistributionPanelProps) {
  return (
    <section className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Distribuicao de prioridade</p>
          <h2>Leitura rapida do backlog</h2>
        </div>
      </div>
      <div className="distribution-list">
        {distribution.map((item) => (
          <article key={item.priorityLevel} className="distribution-card">
            <div className={`priority-badge ${item.priorityLevel.toLowerCase()}`}>{item.priorityLevel}</div>
            <strong>{item.segmentCount}</strong>
            <p>{item.interpretation}</p>
          </article>
        ))}
      </div>
    </section>
  );
}
