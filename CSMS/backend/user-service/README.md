# User Service

User Service owns school-person profile data for students, teachers, parents, and staff.

Auth Service owns credentials, roles, JWTs, refresh tokens, and login. User Service stores the profile data connected to an Auth identity through:

```text
auth_user_id
username
```

## Main responsibilities

- common profile data
- student profile details
- teacher profile details
- parent profile details
- staff profile details
- addresses
- parent-student relationships

## Local prerequisites

- Java 21
- Maven
- Docker

## Run PostgreSQL locally

From the repository root:

```bash
docker compose -f infrastructure/docker/docker-compose.yml up -d
```

This starts:

- `auth-postgres` on host port `5432`
- `user-postgres` on host port `5433`

## Run the service locally

From `backend/user-service`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

The service runs on:

```text
http://localhost:8083
```

Swagger UI:

```text
http://localhost:8083/swagger-ui.html
```

## First implementation target

The first useful API slice should be:

- create student profile after Auth account creation
- create teacher profile
- create parent profile
- retrieve profile by `auth_user_id`
- retrieve profile by `username`

## Cross-service verification

After Auth Service and User Service are both running locally, verify the first integrated flow from the repository root:

```bash
bash scripts/verify-auth-user-student-flow.sh
```

See:

```text
docs/architecture/auth-user-service-integration.md
```
