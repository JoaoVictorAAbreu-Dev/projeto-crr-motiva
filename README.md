# Motiva Verde Inteligente

Motiva Verde Inteligente is a web platform for operational vegetation management along highways. The project was designed as a decision support center for CCR Motiva, helping conservation teams decide where to intervene first, when to intervene, and how to distribute field crews across the week.

The prototype focuses on the challenge described by the academic brief:
- reduce waste caused by fixed mowing schedules
- identify critical stretches earlier
- justify field decisions with data
- present a clear flow of `input -> processing -> operational output`

The project also incorporates clarifications answered by Motiva in the challenge questionnaire:
- simulated or aggregated data is acceptable when the assumptions are consistent
- the goal is to propose operational improvements, not just replicate the current process
- climate, safety, and contractual restrictions should influence the decision logic
- Motiva cameras should not be treated as an MVP dependency because of LGPD and data sensitivity
- the solution should favor precision, cost efficiency, scalability, and simple implementation

## Solution Overview

Motiva Verde Inteligente is positioned as an explainable operational prioritization platform rather than a generic CRUD system.

The platform combines historical maintenance events, weather context, vegetation growth profiles, operational criticality, and manual inspection signals to calculate an `Intervention Priority Index (IPI)` for each road segment.

Main outputs:
- criticality dashboard
- operational map by segment
- priority ranking with explanations
- weekly crew planning
- executive efficiency summary
- priority distribution view
- operational alert feed for presentation
- scenario simulation for weather and crew constraints

Business outcomes emphasized in the board presentation:
- reduce unnecessary mowing cycles
- anticipate roadside safety risk
- improve use of crews, equipment, and logistics
- justify operational decisions with transparent criteria

## Tech Stack

- Backend: Java 21+, Spring Boot, Spring Data JPA, Bean Validation, OpenAPI
- Database: PostgreSQL
- Frontend: React, TypeScript, Vite
- Infra: Docker, Docker Compose
- Tests: JUnit 5, Mockito, Spring MVC Test

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

## MVP Features

- road segment catalog with operational metadata
- transparent IPI recalculation
- criticality ranking
- operational map with clickable segments
- weekly plan generation based on crew capacity
- fixed-schedule vs intelligent-plan comparison
- scenario simulator for rainfall and available crews

## Business Rules

The IPI score ranges from `0` to `100` and is calculated from:
- days since the last mowing
- recent rainfall
- humidity and temperature
- vegetation growth profile
- operational criticality
- recurrence of past interventions
- sensitive-area proximity
- contractual pressure
- inspector signal

The engine is intentionally explainable. Every calculated score returns the reasons that increased urgency.

Safety is explicit in the decision model:
- driver visibility risk
- fire-prevention pressure
- roadside infrastructure exposure
- access conditions for response teams

Contract compliance is also explicit in the score, because the challenge questionnaire reinforced the importance of concession obligations and labor constraints.

## Simulation Premises

The current MVP uses simulated but consistent data, which is aligned with Motiva's own recommendation for the challenge.

Reference premises used in the prototype:
- climate affects vegetation growth and intervention timing
- fixed schedules create avoidable waste in part of the network
- high-traffic or operationally sensitive stretches demand faster action
- not every segment needs the same intervention frequency

Cost references incorporated into the ROI narrative:
- manual mowing: `R$ 0,20 to R$ 0,40 / m²`
- mechanical mowing: `R$ 0,10 to R$ 0,23 / m²`
- annual reference cycles: `13 to 18 mowing cycles`

The executive savings summary uses these values as reference inputs for scenario comparison. They are not presented as audited production values.

## API Summary

Core endpoints:

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

Swagger UI:

- `http://localhost:8080/swagger-ui.html`

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
npm run build
```

## Demo Flow

Recommended presentation flow for the academic board:

1. Show the executive dashboard and the current operational backlog.
2. Open the map and explain how each segment is classified.
3. Select a critical segment and show the reasons behind the IPI.
4. Recalculate priorities after changing rainfall and inspector signals.
5. Generate a weekly plan with limited crew capacity.
6. Compare fixed-schedule maintenance versus intelligent prioritization.
7. Close by explaining that the MVP is intentionally explainable, low-dependency, and scalable.

## Risks and Limitations

- Initial road geometries are simulated to keep the prototype demonstrable.
- Operational and cost premises are simulated from challenge references, not from audited Motiva production systems.
- The weather layer is represented by internal snapshots and scenario simulation, not live meteorological integration.
- Authentication was intentionally deferred to keep the prototype focused on the operational decision engine.
- Computer vision, IoT, and smart camera integrations are treated as future evolutions, not MVP dependencies.

## Tests Added

- unit tests for priority scoring
- unit tests for weekly plan generation
- controller tests for priority ranking and plan generation

## Pending Improvements

- ingest KMZ and Excel files automatically
- add JWT authentication and role-based permissions
- export PDF/CSV reports
- connect with real weather APIs and GIS layers
- persist scenario simulations for audit history
- add optional future modules for IoT sensing and external computer-vision ingestion without depending on Motiva camera infrastructure
