@echo off
setlocal
cd /d "%~dp0"

echo === Motiva Verde Inteligente - Check Local ===

if exist ".env" (
  echo [OK] .env encontrado
) else (
  echo [WARN] .env nao encontrado
)

where java >nul 2>nul
if %errorlevel%==0 (
  echo [OK] Java encontrado
) else (
  echo [WARN] Java nao encontrado
)

where mvn >nul 2>nul
if %errorlevel%==0 (
  echo [OK] Maven encontrado
) else (
  echo [WARN] Maven nao encontrado
)

where docker >nul 2>nul
if %errorlevel%==0 (
  echo [OK] Docker encontrado
) else (
  echo [WARN] Docker nao encontrado
)

where npm >nul 2>nul
if %errorlevel%==0 (
  echo [OK] npm encontrado
) else (
  echo [WARN] npm nao encontrado
)

echo.
echo Atalhos disponiveis:
echo - start-local.cmd
echo - start-frontend.cmd
