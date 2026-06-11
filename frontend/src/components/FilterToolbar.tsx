import type { PriorityLevel } from "../types";

interface FilterToolbarProps {
  highways: string[];
  selectedHighway: string;
  selectedPriority: PriorityLevel | "ALL";
  contractOnly: boolean;
  sensitiveOnly: boolean;
  onHighwayChange: (value: string) => void;
  onPriorityChange: (value: PriorityLevel | "ALL") => void;
  onContractOnlyChange: (value: boolean) => void;
  onSensitiveOnlyChange: (value: boolean) => void;
}

export function FilterToolbar({
  highways,
  selectedHighway,
  selectedPriority,
  contractOnly,
  sensitiveOnly,
  onHighwayChange,
  onPriorityChange,
  onContractOnlyChange,
  onSensitiveOnlyChange,
}: FilterToolbarProps) {
  return (
    <section className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Filtros de operacao</p>
          <h2>Recorte da apresentacao</h2>
        </div>
        <p className="panel-copy">Use filtros para destacar risco contratual, seguranca viaria e criticidade.</p>
      </div>

      <div className="filters-grid">
        <label>
          Rodovia
          <select value={selectedHighway} onChange={(event) => onHighwayChange(event.target.value)}>
            <option value="ALL">Todas</option>
            {highways.map((highway) => (
              <option key={highway} value={highway}>
                {highway}
              </option>
            ))}
          </select>
        </label>
        <label>
          Prioridade
          <select value={selectedPriority} onChange={(event) => onPriorityChange(event.target.value as PriorityLevel | "ALL")}>
            <option value="ALL">Todas</option>
            <option value="CRITICAL">Critical</option>
            <option value="HIGH">High</option>
            <option value="MEDIUM">Medium</option>
            <option value="LOW">Low</option>
          </select>
        </label>
        <label className="toggle-row">
          <input type="checkbox" checked={contractOnly} onChange={(event) => onContractOnlyChange(event.target.checked)} />
          Somente risco contratual
        </label>
        <label className="toggle-row">
          <input type="checkbox" checked={sensitiveOnly} onChange={(event) => onSensitiveOnlyChange(event.target.checked)} />
          Somente area sensivel
        </label>
      </div>
    </section>
  );
}
