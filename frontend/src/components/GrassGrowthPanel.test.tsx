import { render, screen } from "@testing-library/react";
import { describe, expect, it } from "vitest";
import { GrassGrowthPanel } from "./GrassGrowthPanel";

describe("GrassGrowthPanel", () => {
  it("renders empty state when prediction is unavailable", () => {
    render(<GrassGrowthPanel prediction={null} ranking={[]} />);

    expect(screen.getByText(/Selecione um trecho/i)).toBeInTheDocument();
  });

  it("renders prediction cards and ranking items", () => {
    render(
      <GrassGrowthPanel
        prediction={{
          segmentId: "segment-1",
          segmentName: "Trecho Norte",
          predictedGrassHeightCm: 31.4,
          expectedDailyGrowthCm: 1.27,
          daysToCriticalHeight: 2,
          confidenceScore: 0.84,
          modelVersion: "sim-regression-v1",
          drivers: ["Recent rainfall increased predicted grass acceleration."],
        }}
        ranking={[
          {
            segmentId: "segment-1",
            segmentName: "Trecho Norte",
            predictedGrassHeightCm: 31.4,
            daysToCriticalHeight: 2,
            recommendation: "Dispatch a crew immediately.",
          },
        ]}
      />,
    );

    expect(screen.getAllByText((_, element) => element?.textContent === "31.4 cm")).toHaveLength(2);
    expect(screen.getByText((_, element) => element?.textContent === "1.27 cm/dia")).toBeInTheDocument();
    expect(screen.getByText((_, element) => element?.textContent === "84%")).toBeInTheDocument();
    expect(screen.getByText(/Dispatch a crew immediately/i)).toBeInTheDocument();
  });
});
