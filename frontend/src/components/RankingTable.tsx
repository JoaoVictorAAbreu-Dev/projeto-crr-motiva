import type { PriorityAssessment } from "../types";

interface RankingTableProps {
  ranking: PriorityAssessment[];
  onSelect: (id: string) => void;
}

export function RankingTable({ ranking, onSelect }: RankingTableProps) {
  return (
    <section className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Ranking IPI</p>
          <h2>Prioridade de intervencao</h2>
        </div>
      </div>

      <div className="table-shell">
        <table className="ranking-table">
          <thead>
            <tr>
              <th>Trecho</th>
              <th>Score</th>
              <th>Nivel</th>
              <th>Justificativa</th>
            </tr>
          </thead>
          <tbody>
            {ranking.map((item) => (
              <tr key={item.segmentId} onClick={() => onSelect(item.segmentId)} className="table-row-clickable">
                <td>{item.segmentName}</td>
                <td>{item.score.toFixed(1)}</td>
                <td>
                  <span className={`priority-badge ${item.priorityLevel.toLowerCase()}`}>{item.priorityLevel}</span>
                </td>
                <td>{item.reasons.join(" ")}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
