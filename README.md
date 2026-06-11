# GreenOps Control Center

Plataforma web de apoio à decisão para gestão operacional de vegetação em rodovias.

O GreenOps Control Center foi desenvolvido como projeto de portfólio para demonstrar capacidade de entrega full stack em um cenário realista de operação: ingestão de dados, priorização explicável, planejamento semanal, integração com clima, autenticação JWT, testes automatizados e documentação técnica.

## Visão Geral

Em operações lineares de manutenção, como conservação de faixa de domínio, o maior problema não costuma ser falta de execução, mas sim falta de priorização. Cronogramas fixos tendem a gerar dois desperdícios ao mesmo tempo:

- intervenção antes da hora, elevando custo operacional
- intervenção tarde demais, aumentando risco de visibilidade, recorrência e pressão contratual

O GreenOps Control Center resolve esse problema organizando a operação em um fluxo claro:

1. entrada de dados operacionais e climáticos
2. cálculo de prioridade por trecho com lógica explicável
3. visualização em mapa, ranking e indicadores executivos
4. geração de plano semanal conforme capacidade das equipes

## Objetivo do Produto

O sistema responde três perguntas operacionais centrais:

- onde intervir primeiro
- quando intervir
- como distribuir as equipes disponíveis

Em vez de funcionar como um CRUD genérico, o produto foi desenhado como um centro de controle operacional, com foco em decisão, rastreabilidade e produtividade.

## Principais Funcionalidades

- cadastro e consulta de trechos rodoviários com atributos operacionais
- cálculo do Índice de Prioridade de Intervenção (IPI)
- ranking explicável de criticidade
- mapa operacional com inspeção por trecho
- painel executivo com backlog, risco e economia estimada
- geração de plano semanal com limite de equipes
- comparação entre cronograma fixo e priorização inteligente
- integração com OpenWeather com fallback para simulação
- estimativa de altura da grama com modelo preditivo leve
- autenticação JWT para acesso às APIs operacionais

## Diferenciais Técnicos

- regra de negócio centralizada em serviços do backend
- modelo de priorização explicável, sem dependência de IA caixa-preta
- frontend organizado por componentes e camada de serviços
- autenticação stateless com Spring Security + JWT
- documentação OpenAPI com suporte a Bearer Token
- suíte mínima de testes automatizados no frontend e backend
- scripts locais para acelerar setup e demonstração

## Arquitetura

### Backend

Stack principal:

- Java 21
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT
- Bean Validation
- OpenAPI / Swagger

Organização em camadas:

- `controller`: exposição de endpoints REST
- `dto`: contratos de entrada e saída
- `service`: regra de negócio e orquestração
- `repository`: acesso a dados
- `model`: entidades de domínio
- `config`: infraestrutura, OpenAPI, CORS e segurança
- `exception`: tratamento centralizado de erros

### Frontend

Stack principal:

- React
- TypeScript
- Vite

Padrões adotados:

- componentes reutilizáveis
- separação entre UI e consumo de API
- estado de tela concentrado no `App.tsx`
- autenticação persistida em sessão local
- testes com Vitest e Testing Library

### Banco de Dados

- PostgreSQL

O projeto usa PostgreSQL como banco principal e H2 para cenários de teste no backend.

## Modelo de Decisão

O Índice de Prioridade de Intervenção varia de `0` a `100` e considera:

- dias desde a última roçada
- chuva recente
- temperatura e umidade
- classe de vegetação
- criticidade operacional
- recorrência histórica
- pressão contratual
- sensibilidade do trecho
- sinalização manual do fiscal
- altura prevista da vegetação
- tempo estimado até atingir faixa crítica

O sistema sempre retorna justificativas textuais para o score calculado, permitindo defender a decisão de forma objetiva.

## Camada de ML

O projeto inclui um modelo preditivo leve para estimativa de crescimento da grama.

Características:

- abordagem simples e explicável
- treinamento sintético com regressão aproximada
- previsão de altura estimada
- crescimento diário esperado
- dias até altura crítica
- drivers textuais da previsão

Essa camada complementa a priorização. A decisão principal continua baseada em lógica operacional transparente.

## Segurança

O acesso às APIs operacionais é protegido com JWT.

Fluxo implementado:

1. autenticação via `POST /api/auth/login`
2. emissão de token Bearer
3. armazenamento local da sessão no frontend
4. envio automático do token nas chamadas autenticadas
5. endpoint `GET /api/auth/me` para resolução da sessão corrente

O Swagger foi configurado com esquema Bearer para facilitar inspeção e demonstração técnica da API.

## Endpoints Principais

### Autenticação

- `POST /api/auth/login`
- `GET /api/auth/me`

### Operação

- `GET /api/segments`
- `GET /api/segments/{id}`
- `GET /api/priority-ranking`
- `POST /api/priority-assessments/recalculate`
- `POST /api/maintenance-events`
- `POST /api/weekly-plans/generate`
- `GET /api/weekly-plans/{id}`

### Relatórios e Dashboard

- `GET /api/reports/critical-segments`
- `GET /api/reports/efficiency-summary`
- `GET /api/reports/priority-distribution`
- `GET /api/reports/operational-alerts`
- `GET /api/dashboard/overview`
- `GET /api/dashboard/assumptions`

### Clima e Previsão

- `GET /api/weather/live/{segmentId}`
- `POST /api/weather/live/{segmentId}/refresh`
- `POST /api/weather/live/refresh-all`
- `GET /api/ml/grass-growth/{segmentId}`
- `GET /api/ml/grass-growth/ranking`

## Documentação da API

Swagger UI:

- `http://localhost:8080/swagger-ui.html`

## Estrutura do Projeto

```text
backend/
  src/main/java/com/motiva/verdeinteligente/
  src/main/resources/
  src/test/java/com/motiva/verdeinteligente/
frontend/
  src/
docker-compose.yml
.env.example
check-local.cmd
start-local.cmd
start-frontend.cmd
```

## Setup Rápido

### Scripts locais

Na raiz do projeto:

```bat
check-local.cmd
start-local.cmd
start-frontend.cmd
```

Função de cada script:

- `check-local.cmd`: valida ambiente, dependências e variáveis
- `start-local.cmd`: sobe a stack preferencialmente com Docker Compose
- `start-frontend.cmd`: inicia o frontend separadamente

## Execução com Docker

Pré-requisito:

- Docker Desktop

Passos:

1. copie `.env.example` para `.env`
2. execute:

```bash
docker compose up --build
```

Acessos:

- frontend: `http://localhost:5173`
- backend: `http://localhost:8080`
- PostgreSQL: `localhost:5432`

## Execução Local

Pré-requisitos:

- Java 21+
- Maven 3.9+
- Node 22+
- PostgreSQL 16+

### Backend

```bash
cd backend
mvn spring-boot:run
```

### Frontend

```bash
cd frontend
npm install
npm run dev
```

## Variáveis de Ambiente

Use `.env.example` como base.

### Backend

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `APP_CORS_ALLOWED_ORIGINS`
- `APP_AUTH_DEMO_USERNAME`
- `APP_AUTH_DEMO_PASSWORD`
- `APP_AUTH_DEMO_FULL_NAME`
- `APP_AUTH_DEMO_ROLE`
- `APP_AUTH_JWT_SECRET`
- `APP_AUTH_TOKEN_EXPIRATION_HOURS`
- `OPENWEATHER_API_KEY`
- `OPENWEATHER_BASE_URL`

### Frontend

- `VITE_API_BASE_URL`

## Credenciais de Demonstração

Credencial padrão:

- usuário: `motiva.admin`
- senha: `motiva@123`

Esses valores podem ser alterados via `.env`.

## Testes

### Backend

```bash
cd backend
mvn test
```

### Frontend

```bash
cd frontend
npm install
npm test
npm run build
```

## Cobertura de Testes Implementada

- testes unitários do motor de priorização
- testes unitários do plano semanal
- testes de controller para autenticação, segmentos, ranking, relatórios e geração de plano
- testes de componentes do frontend
- testes da camada de API do frontend

## Limitações Atuais

- geometrias e parte dos dados são simulados para manter o projeto reproduzível
- autenticação usa usuário demonstrativo, não tabela persistida de usuários
- integrações GIS, IoT e visão computacional ainda não fazem parte do MVP
- a camada de ML é propositalmente simples e explicável

## Roadmap

- persistência real de usuários e papéis
- RBAC mais granular por perfil operacional
- ingestão automática de arquivos KMZ e planilhas
- exportação PDF e CSV
- integrações GIS e clima mais completas
- observabilidade, métricas e pipeline CI/CD

## Valor de Portfólio

Este projeto demonstra:

- modelagem de problema operacional real
- arquitetura backend em camadas
- frontend orientado a produto
- autenticação e segurança de API
- documentação técnica clara
- testes automatizados
- preocupação com setup, manutenção e entrega
