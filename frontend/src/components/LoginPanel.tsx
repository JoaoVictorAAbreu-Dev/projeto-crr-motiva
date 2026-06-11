import { useState } from "react";

interface LoginPanelProps {
  error: string | null;
  onLogin: (username: string, password: string) => Promise<void>;
}

export function LoginPanel({ error, onLogin }: LoginPanelProps) {
  const [username, setUsername] = useState("motiva.admin");
  const [password, setPassword] = useState("motiva@123");
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitting(true);
    try {
      await onLogin(username, password);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <section className="login-shell panel">
      <div className="panel-header">
        <div>
          <p className="eyebrow">Acesso operacional</p>
          <h2>Entrar no centro de controle</h2>
        </div>
      </div>
      <p className="panel-copy">
        Use o perfil demonstrativo para acessar os painéis, recalcular prioridade e gerar planos semanais.
      </p>
      <form className="login-form" onSubmit={handleSubmit}>
        <label>
          Usuario
          <input value={username} onChange={(event) => setUsername(event.target.value)} />
        </label>
        <label>
          Senha
          <input type="password" value={password} onChange={(event) => setPassword(event.target.value)} />
        </label>
        {error ? <div className="error-banner">{error}</div> : null}
        <button className="button primary" type="submit" disabled={submitting}>
          {submitting ? "Entrando..." : "Entrar"}
        </button>
      </form>
    </section>
  );
}
