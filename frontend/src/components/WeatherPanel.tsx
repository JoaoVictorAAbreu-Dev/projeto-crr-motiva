import type { LiveWeather } from "../types";

interface WeatherPanelProps {
  weather: LiveWeather | null;
  onRefresh: () => Promise<void>;
}

export function WeatherPanel({ weather, onRefresh }: WeatherPanelProps) {
  return (
    <section className="panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Clima ao vivo</p>
          <h2>Atualizacao meteorologica</h2>
        </div>
        <button className="button secondary" onClick={() => void onRefresh()}>
          Atualizar clima
        </button>
      </div>

      {!weather ? (
        <p className="empty-state">Selecione um trecho para visualizar o snapshot meteorologico.</p>
      ) : (
        <div className="weather-grid">
          <article className="report-card-lite">
            <strong>{weather.temperatureCelsius.toFixed(1)}°C</strong>
            <span>Temperatura</span>
          </article>
          <article className="report-card-lite">
            <strong>{weather.rainfallMm.toFixed(1)} mm</strong>
            <span>Chuva</span>
          </article>
          <article className="report-card-lite">
            <strong>{weather.humidityPercent.toFixed(0)}%</strong>
            <span>Umidade</span>
          </article>
          <article className="report-card-lite">
            <strong>{weather.liveData ? "API" : "Fallback"}</strong>
            <span>{weather.source}</span>
          </article>
        </div>
      )}
    </section>
  );
}
