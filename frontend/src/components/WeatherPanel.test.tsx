import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import { WeatherPanel } from "./WeatherPanel";

describe("WeatherPanel", () => {
  it("renders empty state when weather is unavailable", () => {
    render(<WeatherPanel weather={null} onRefresh={vi.fn().mockResolvedValue(undefined)} />);

    expect(screen.getByText(/Selecione um trecho/i)).toBeInTheDocument();
  });

  it("renders weather metrics and triggers refresh", async () => {
    const user = userEvent.setup();
    const onRefresh = vi.fn().mockResolvedValue(undefined);

    render(
      <WeatherPanel
        weather={{
          segmentId: "segment-1",
          segmentName: "Trecho Oeste",
          liveData: true,
          snapshotDate: "2026-06-11",
          rainfallMm: 22.4,
          temperatureCelsius: 27.8,
          humidityPercent: 81,
          source: "openweather",
        }}
        onRefresh={onRefresh}
      />,
    );

    expect(screen.getByText("Temperatura")).toBeInTheDocument();
    expect(screen.getByText("Chuva")).toBeInTheDocument();
    expect(screen.getByText("Umidade")).toBeInTheDocument();
    expect(screen.getByText((_, element) => element?.textContent === "27.8°C" || element?.textContent === "27.8�C")).toBeInTheDocument();
    expect(screen.getByText((_, element) => element?.textContent === "22.4 mm")).toBeInTheDocument();
    expect(screen.getByText((_, element) => element?.textContent === "81%")).toBeInTheDocument();
    expect(screen.getByText("API")).toBeInTheDocument();

    await user.click(screen.getByRole("button", { name: /Atualizar clima/i }));

    expect(onRefresh).toHaveBeenCalledTimes(1);
  });
});
