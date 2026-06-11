import type { GrassGrowthPrediction, GrassGrowthRankingItem } from "../types";

interface GrassGrowthPanelProps {
  prediction: GrassGrowthPrediction | null;
  ranking: GrassGrowthRankingItem[];
}

export function GrassGrowthPanel({ prediction, ranking }: GrassGrowthPanelProps) {
  return (
    <section className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">ML de crescimento</p>
          <h2>Estimativa de altura da grama</h2>
        </div>
      </div>

      {prediction ? (
        <>
          <div className="weather-grid">
            <article className="report-card-lite">
              <strong>{prediction.predictedGrassHeightCm.toFixed(1)} cm</strong>
              <span>Altura prevista</span>
            </article>
            <article className="report-card-lite">
              <strong>{prediction.expectedDailyGrowthCm.toFixed(2)} cm/dia</strong>
              <span>Crescimento esperado</span>
            </article>
            <article className="report-card-lite">
              <strong>{prediction.daysToCriticalHeight}</strong>
              <span>Dias ate altura critica</span>
            </article>
            <article className="report-card-lite">
              <strong>{Math.round(prediction.confidenceScore * 100)}%</strong>
              <span>Confianca do modelo</span>
            </article>
          </div>
          <ul className="reason-list">
            {prediction.drivers.map((driver) => (
              <li key={driver}>{driver}</li>
            ))}
          </ul>
        </>
      ) : (
        <p className="empty-state">Selecione um trecho para carregar a previsao do modelo.</p>
      )}

      <div className="rank-mini-list">
        {ranking.map((item) => (
          <article key={item.segmentId} className="distribution-card">
            <strong>{item.segmentName}</strong>
            <span>{item.predictedGrassHeightCm.toFixed(1)} cm</span>
            <p>{item.recommendation}</p>
          </article>
        ))}
      </div>
    </section>
  );
}
