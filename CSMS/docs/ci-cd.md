# CI/CD Pipeline

This project uses GitHub Actions for CI/CD.

## Current pipeline

### 1. CI

Workflow:

```text
.github/workflows/ci.yml
```

Runs on:
- push to `main`
- push to `develop`
- pull request to `main`
- pull request to `develop`

What it does:
- checks out the repository
- installs Java 21
- runs Maven tests for each backend service
- uploads Surefire test reports

Services included:
- `auth-service`
- `user-service`
- `gateway-service`
- `admission-service`

### 2. Docker Build

Workflow:

```text
.github/workflows/docker-build.yml
```

Runs on:
- push to `main`
- version tags like `v1.0.0`
- manual run from GitHub Actions

What it does:
- builds each service jar
- builds Docker images
- optionally pushes images to GitHub Container Registry

Image naming pattern:

```text
ghcr.io/<github-owner>/school-<service-name>:<short-commit-sha>
ghcr.io/<github-owner>/school-<service-name>:latest
```

Example:

```text
ghcr.io/subha/school-auth-service:abc1234
```

### 3. Dev Deployment

Workflow:

```text
.github/workflows/deploy-dev.yml
```

Runs on:
- manual trigger only

What it does:
- copies `infrastructure/deploy/docker-compose.dev.yml` to the dev server
- logs in to GitHub Container Registry
- pulls service images
- starts/restarts services with Docker Compose

Required GitHub repository secrets:

```text
DEV_HOST
DEV_SSH_USER
DEV_SSH_PRIVATE_KEY
DEV_DEPLOY_DIR
AUTH_DB_PASSWORD
USER_DB_PASSWORD
ADMISSION_DB_PASSWORD
JWT_SECRET
```

Optional GitHub repository variable:

```text
CORS_ALLOWED_ORIGINS
```

## Recommended branch flow

```text
feature branch -> pull request -> CI tests -> merge to develop -> merge to main -> Docker build/push
```

## Full production CD roadmap

We should add these in stages:

### Stage 1 — Current

- CI tests
- Docker image build
- Docker image push to registry

### Stage 2 — Code quality

- Checkstyle or Spotless
- JaCoCo test coverage
- SonarQube/SonarCloud
- dependency vulnerability scan

### Stage 3 — Kubernetes deployment

- Helm chart per service or shared reusable chart
- deploy to dev namespace from `develop`
- deploy to staging/prod from `main` or version tags

### Stage 4 — Production microservice patterns

- Central configuration using Spring Cloud Config or Kubernetes ConfigMaps/Secrets
- Distributed tracing with OpenTelemetry
- Circuit breaker with Resilience4j
- Event-driven communication using Kafka or RabbitMQ
- Saga pattern for multi-service workflows
- API Gateway rate limiting
- centralized logging with Loki/ELK
- metrics with Prometheus/Grafana

## Important note

CD deployment depends on the final cloud choice:

- AWS: ECR + EKS
- GCP: Artifact Registry + GKE
- Azure: ACR + AKS
- simpler learning path: Docker Compose on a VM first

For learning, the recommended path is:

```text
GitHub Actions -> GHCR Docker Images -> Docker Compose deployment -> later Kubernetes/Helm
```
