# GreenOps Control Center

GreenOps Control Center is a portfolio-grade decision support platform for highway vegetation operations. It demonstrates how to combine operational rules, weather context, explainable prioritization, weekly crew planning, and secure API delivery in a single full-stack product.

The project is positioned as an operational intelligence product rather than a dashboard-only prototype. It focuses on a clear flow of `input -> processing -> operational output`:

- segment and maintenance data enter the system
- the prioritization engine scores intervention urgency
- planners receive an explainable ranking, map view, weather context, and weekly execution plan

## Product Overview

GreenOps Control Center helps maintenance teams answer three core questions:

- where should the next intervention happen first
- when should the intervention happen
- how should the available crews be allocated across the week

Main outputs:

- operational dashboard with executive metrics
- explainable intervention priority index per segment
- criticality map with segment inspection
- weather snapshot and grass-growth projection
- weekly crew plan generation
- efficiency summary against fixed scheduling
- protected API with JWT authentication

## Why This Project Works as Portfolio

This repository demonstrates end-to-end engineering work across:

- backend architecture with Spring Boot, validation, security, and API design
- frontend application structure with React, TypeScript, component decomposition, and API integration
- explainable business logic for prioritization and planning
- automated tests in backend controllers and frontend components/services
- environment-based configuration and local startup scripts
- OpenAPI documentation and Dockerized setup

## Tech Stack

- Backend: Java 21+, Spring Boot, Spring Data JPA, Spring Security, JWT, Bean Validation, OpenAPI
- Database: PostgreSQL
- Frontend: React, TypeScript, Vite
- Infra: Docker, Docker Compose
- Tests: JUnit 5, Mockito, Spring MVC Test, Vitest, Testing Library

## Core Features

- road segment catalog with operational metadata
- transparent intervention priority index recalculation
- explainable priority ranking
- operational map with clickable segments
- weather snapshot refresh and fallback simulation
- grass-height prediction with simple ML-based estimation
- weekly plan generation based on crew capacity
- fixed-schedule versus intelligent-plan comparison
- JWT login for operational access and protected APIs

## Decision Model

The intervention priority index ranges from `0` to `100` and considers:

- days since last mowing
- recent rainfall
- temperature and humidity
- vegetation growth profile
- operational criticality
- recurrence of prior interventions
- sensitive-area pressure
- contractual pressure
- field inspector signal
- predicted grass height and time-to-critical threshold

The model is intentionally explainable. Every score returns a reason list so the recommendation can be defended operationally.

## API Summary

Core endpoints:

- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/segments`
- `GET /api/segments/{id}`
- `GET /api/priority-ranking`
- `POST /api/priority-assessments/recalculate`
- `POST /api/maintenance-events`
- `POST /api/weekly-plans/generate`
- `GET /api/weekly-plans/{id}`
- `GET /api/reports/critical-segments`
- `GET /api/reports/efficiency-summary`
- `GET /api/reports/priority-distribution`
- `GET /api/reports/operational-alerts`
- `GET /api/dashboard/overview`
- `GET /api/dashboard/assumptions`
- `GET /api/weather/live/{segmentId}`
- `POST /api/weather/live/{segmentId}/refresh`
- `POST /api/weather/live/refresh-all`
- `GET /api/ml/grass-growth/{segmentId}`
- `GET /api/ml/grass-growth/ranking`

Swagger UI:

- `http://localhost:8080/swagger-ui.html`

## Project Structure

```text
backend/
  src/main/java/com/motiva/verdeinteligente/
  src/main/resources/
  src/test/java/com/motiva/verdeinteligente/
frontend/
  src/
docker-compose.yml
.env.example
```

## Setup

Quick Windows shortcuts from the project root:

```bat
check-local.cmd
start-local.cmd
start-frontend.cmd
```

What each script does:

- `check-local.cmd`: verifies `.env`, Java, Maven, Docker, and npm
- `start-local.cmd`: prefers `docker compose up --build`; if Docker is missing, it starts the frontend and tells you how to run the backend
- `start-frontend.cmd`: installs dependencies and starts the React app directly

### Option 1: Docker Compose

Requirements:

- Docker Desktop

Steps:

1. Copy `.env.example` to `.env`.
2. Run:

```bash
docker compose up --build
```

Frontend:

- `http://localhost:5173`

Backend:

- `http://localhost:8080`

PostgreSQL:

- `localhost:5432`

### Option 2: Local Development

Requirements:

- Java 21+
- Maven 3.9+
- Node 22+
- PostgreSQL 16+

Backend:

```bash
cd backend
mvn spring-boot:run
```

Frontend:

```bash
cd frontend
npm install
npm run dev
```

## Environment Variables

Use `.env.example` as the base configuration.

Backend:

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

Frontend:

- `VITE_API_BASE_URL`

## Testing

Backend tests:

```bash
cd backend
mvn test
```

Frontend checks:

```bash
cd frontend
npm install
npm test
npm run build
```

## Demo Credentials

Default demo operator:

- username: `motiva.admin`
- password: `motiva@123`

These credentials are environment-driven and can be changed through `.env`.

## Automated Tests Included

- unit tests for priority scoring
- unit tests for weekly plan generation
- controller tests for auth, road segments, priority ranking, reports, and weekly plan generation
- frontend component tests for login, weather, and grass-growth panels
- frontend service tests for API request and error handling

## Risks and Limitations

- initial road geometries are simulated to keep the product demonstrable
- operational and cost premises are representative, not audited production values
- the weather layer uses OpenWeather when configured and falls back to simulation otherwise
- authentication currently uses a protected demo operator instead of a persistent user table
- computer vision, IoT, and external GIS integrations are treated as future expansions

## Roadmap

- add database-backed users and role-based permissions
- ingest KMZ and spreadsheet sources automatically
- export PDF and CSV operational reports
- connect with richer GIS and weather providers
- persist scenario simulations for audit history
- add observability, monitoring, and deployment workflows
