import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { describe, expect, it, vi } from "vitest";
import { LoginPanel } from "./LoginPanel";

describe("LoginPanel", () => {
  it("submits the operator credentials", async () => {
    const user = userEvent.setup();
    const onLogin = vi.fn().mockResolvedValue(undefined);

    render(<LoginPanel error={null} onLogin={onLogin} />);

    await user.clear(screen.getByLabelText("Usuario"));
    await user.type(screen.getByLabelText("Usuario"), "operador");
    await user.clear(screen.getByLabelText("Senha"));
    await user.type(screen.getByLabelText("Senha"), "senha123");
    await user.click(screen.getByRole("button", { name: "Entrar" }));

    expect(onLogin).toHaveBeenCalledWith("operador", "senha123");
  });
});
