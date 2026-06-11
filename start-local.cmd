@echo off
setlocal
cd /d "%~dp0"

echo [Motiva Verde Inteligente] Iniciando ambiente local...

if not exist ".env" (
  if exist ".env.example" (
    copy /Y ".env.example" ".env" >nul
    echo [.env] Arquivo criado a partir de .env.example
  )
)

where docker >nul 2>nul
if %errorlevel%==0 (
  echo [Docker] Detectado. Subindo stack completa com docker compose...
  docker compose up --build
  goto :eof
)

echo [Docker] Nao encontrado.
echo [Info] Vou iniciar somente o frontend em uma nova janela.
echo [Info] Para o backend, instale Maven e execute: cd backend ^&^& mvn spring-boot:run
start "Motiva Frontend" cmd /k "cd /d %~dp0frontend && npm.cmd install && npm.cmd run dev"

echo [Concluido] Frontend iniciado.
echo [Proximo passo] Suba o backend manualmente se o Maven estiver instalado.
