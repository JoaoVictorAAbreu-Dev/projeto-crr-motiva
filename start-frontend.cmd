@echo off
setlocal
cd /d "%~dp0frontend"

echo [Frontend] Instalando dependencias se necessario...
npm.cmd install
if errorlevel 1 goto :fail

echo [Frontend] Iniciando Vite...
npm.cmd run dev
goto :eof

:fail
echo [Erro] Falha ao preparar ou iniciar o frontend.
exit /b 1
