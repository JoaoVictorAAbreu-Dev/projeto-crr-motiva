# Centro Operacional Verde

Plataforma web para monitoramento de vegetacao em rodovias, com foco em decisao operacional, prioridade de atendimento e planejamento de equipes.

## O que este repo entrega
- backend com API e regras de negocio
- frontend para mapa, indicadores e planejamento
- apoio a simulacoes de crescimento e criticidade
- demonstracao de fluxo operacional para banca ou portfolio

## Stack principal
- Backend: Java 21, Spring Boot, Spring Security, JPA e JWT
- Frontend: React, TypeScript e Vite
- Banco: PostgreSQL
- Qualidade: testes automatizados, Swagger e scripts locais

## Estrutura
- `backend/`: servicos, controllers, repositorios e configuracoes
- `frontend/`: interface web e componentes visuais
- `docker-compose.yml`: ambiente local
- `check-local.cmd` e `start-local.cmd`: atalhos para execucao

## Como executar localmente
1. Configure as variaveis de ambiente.
2. Suba o banco de dados.
3. Rode o backend.
4. Rode o frontend.

## Observacoes
- Este repositorio e mais indicado para demonstracao de arquitetura e fluxo.
- O foco e mostrar decisao operacional com dados coerentes e interface clara.
