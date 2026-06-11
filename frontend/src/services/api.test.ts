import { afterEach, describe, expect, it, vi } from "vitest";
import { api, authSession } from "./api";

describe("api service", () => {
  afterEach(() => {
    vi.restoreAllMocks();
    localStorage.clear();
  });

  it("returns parsed payloads for successful requests", async () => {
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValue({
        ok: true,
        json: () =>
          Promise.resolve([
            {
              id: "segment-1",
              name: "Trecho Oeste",
            },
          ]),
      }),
    );

    const result = await api.getSegments();

    expect(result).toHaveLength(1);
    expect(result[0]?.id).toBe("segment-1");
    expect(fetch).toHaveBeenCalledTimes(1);
  });

  it("throws the backend message for failed requests", async () => {
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValue({
        ok: false,
        status: 400,
        text: () => Promise.resolve(JSON.stringify({ message: "Validation failed" })),
      }),
    );

    await expect(api.getRanking()).rejects.toThrow("Validation failed");
  });

  it("attaches the bearer token when a session exists", async () => {
    authSession.setToken("jwt-token");
    vi.stubGlobal(
      "fetch",
      vi.fn().mockResolvedValue({
        ok: true,
        json: () => Promise.resolve({ username: "motiva.admin", fullName: "Operador Motiva", role: "MANAGER" }),
      }),
    );

    await api.getCurrentUser();

    expect(fetch).toHaveBeenCalledWith(
      expect.stringContaining("/auth/me"),
      expect.objectContaining({
        headers: expect.objectContaining({
          Authorization: "Bearer jwt-token",
        }),
      }),
    );
  });
});
