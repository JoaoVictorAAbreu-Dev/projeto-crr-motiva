import type { SegmentSummary } from "../types";

interface OperationalMapProps {
  segments: SegmentSummary[];
  selectedId: string | null;
  onSelect: (id: string) => void;
}

function priorityColor(level: SegmentSummary["priorityLevel"]) {
  switch (level) {
    case "CRITICAL":
      return "#ff5e4d";
    case "HIGH":
      return "#ffb84d";
    case "MEDIUM":
      return "#86d77c";
    default:
      return "#5d86ff";
  }
}

export function OperationalMap({ segments, selectedId, onSelect }: OperationalMapProps) {
  return (
    <section className="panel map-panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Mapa operacional</p>
          <h2>Trechos do Rodoanel por criticidade</h2>
        </div>
        <p className="panel-copy">
          Cada ponto representa um trecho com prioridade calculada pelo motor de decisão.
        </p>
      </div>

      <div className="map-shell">
        <svg viewBox="0 0 100 100" className="operational-map" aria-label="Operational vegetation risk map">
          <defs>
            <radialGradient id="mapGlow" cx="50%" cy="50%" r="50%">
              <stop offset="0%" stopColor="rgba(95, 146, 255, 0.38)" />
              <stop offset="100%" stopColor="rgba(95, 146, 255, 0)" />
            </radialGradient>
          </defs>

          <circle cx="50" cy="50" r="44" fill="url(#mapGlow)" />
          <path
            d="M18 52 C25 18, 76 18, 83 52 C77 83, 26 82, 18 52Z"
            fill="none"
            stroke="rgba(111, 158, 255, 0.35)"
            strokeWidth="6"
            strokeDasharray="2 6"
          />

          {segments.map((segment) => (
            <g key={segment.id} onClick={() => onSelect(segment.id)} className="map-node-group" role="button">
              <circle
                cx={segment.mapX}
                cy={segment.mapY}
                r={selectedId === segment.id ? 5.2 : 4.1}
                fill={priorityColor(segment.priorityLevel)}
                stroke={selectedId === segment.id ? "#f6f7fb" : "rgba(255,255,255,0.24)"}
                strokeWidth={selectedId === segment.id ? 1.5 : 0.8}
              />
            </g>
          ))}
        </svg>
      </div>

      <div className="map-legend">
        <span><i className="dot critical" />Crítica</span>
        <span><i className="dot high" />Alta</span>
        <span><i className="dot medium" />Média</span>
        <span><i className="dot low" />Baixa</span>
      </div>
    </section>
  );
}
