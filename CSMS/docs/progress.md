# Project Progress

## Current Goal

Build a production-style school management platform using Spring Boot microservices and Next.js.

## Completed

### CI/CD Foundation

Path: `.github/workflows`

Status: foundation added.

Includes:
- CI workflow for all backend services
- Java 21 setup
- Maven test matrix
- test report artifacts
- Docker build/push workflow for service images
- GHCR-ready image naming
- manual dev deployment workflow
- Docker Compose dev deployment file
- CI/CD documentation

### Auth Service

Path: `backend/auth-service`

Status: core complete.

Includes:
- username-based login
- generated usernames such as `STU2026000001`
- bootstrap admin
- JWT access tokens
- refresh tokens
- logout
- password change
- account lockout
- Docker/local config
- unit + integration tests

Local port:

```text
8082
```

### User Service

Path: `backend/user-service`

Status: profile API foundation complete; JWT validation added.

Includes:
- student profile APIs
- teacher profile APIs
- parent profile APIs
- staff profile APIs
- update APIs
- lookup by `authUserId`
- lookup by `username`
- parent-student linking
- JWT validation using Auth Service shared secret
- unit + integration tests

Local port:

```text
8083
```

### Cross-service verification

Script:

```text
scripts/verify-auth-user-student-flow.sh
```

Docs:

```text
docs/architecture/auth-user-service-integration.md
```

Purpose:
- create auth user
- create matching user profile
- verify lookups
- verify student login

### Gateway Service

Path: `backend/gateway-service`

Status: skeleton added; tests passing.

Local port:

```text
8080
```

Routes:
- `/api/v1/auth/**` -> Auth Service
- `/api/v1/admin/**` -> Auth Service
- `/api/v1/profiles/**` -> User Service
- `/api/v1/admissions/**` -> Admission Service

### Admission Service

Path: `backend/admission-service`

Status: skeleton added; tests passing.

Local port:

```text
8084
```

Includes:
- Spring Boot service foundation
- Postgres/Flyway config
- Swagger/OpenAPI
- Actuator health/info
- Dockerfile
- local test profile

## Next Recommended Step

Build Admission Service domain model and APIs for admission enquiries/applications.
