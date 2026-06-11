import type { SegmentDetail } from "../types";

interface SegmentInspectorProps {
  segment: SegmentDetail | null;
}

export function SegmentInspector({ segment }: SegmentInspectorProps) {
  return (
    <section className="panel inspector-panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Inspetor de trecho</p>
          <h2>Contexto operacional</h2>
        </div>
      </div>

      {!segment ? (
        <p className="empty-state">Selecione um trecho no mapa para ver score, explicabilidade e contexto climático.</p>
      ) : (
        <div className="inspector-content">
          <div>
            <h3>{segment.name}</h3>
            <p>{segment.highway} • km {segment.kmStart.toFixed(1)} a {segment.kmEnd.toFixed(1)}</p>
          </div>
          <div className="inspector-metrics">
            <span>IPI {segment.score.toFixed(1)}</span>
            <span>{segment.priorityLevel}</span>
            <span>{segment.recentRainfallMm} mm</span>
            <span>{segment.humidityPercent}% umidade</span>
          </div>
          <ul className="reason-list">
            {segment.reasons.map((reason) => (
              <li key={reason}>{reason}</li>
            ))}
          </ul>
          <p className="segment-notes">{segment.notes}</p>
        </div>
      )}
    </section>
  );
}
