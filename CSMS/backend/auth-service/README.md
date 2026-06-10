# Auth Service

Authentication and authorization service for the school management platform.

## Main capabilities

- username-based login
- student-style generated usernames such as `STU2026000001`
- temporary-password account creation
- JWT access tokens
- opaque refresh tokens stored by hash
- first-login password change flow
- bootstrap admin creation for local development
- refresh-token revocation on logout
- role-based authorization

## Local prerequisites

- Java 21
- Maven
- Docker

## Run PostgreSQL locally

From the repository root:

```bash
docker compose -f infrastructure/docker/docker-compose.yml up -d
```

## Run the service locally

From `backend/auth-service`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

The service runs on:

```text
http://localhost:8081
```

Swagger UI:

```text
http://localhost:8081/swagger-ui.html
```

## Bootstrap admin for local development

When using the `local` profile, the service creates this admin user if it does not already exist:

```text
username: ADM0001
password: Admin@12345
```

Change this password immediately after the first login.

## Important endpoints

### Create user

```http
POST /api/v1/admin/users
```

Example request:

```json
{
  "userType": "STUDENT",
  "admissionYear": 2026,
  "email": null,
  "phoneNumber": "9876543210",
  "createdBy": "admin"
}
```

### Login

```http
POST /api/v1/auth/login
```

```json
{
  "username": "STU2026000001",
  "password": "temporary-password"
}
```

### Refresh token

```http
POST /api/v1/auth/refresh
```

### Current user

```http
GET /api/v1/auth/me
```

### Change password

```http
POST /api/v1/auth/change-password
```

### Logout

```http
POST /api/v1/auth/logout
```

## Build container image

```bash
mvn clean package
docker build -t school/auth-service:local .
```

## Production notes

- set `JWT_SECRET` through a secret manager or environment variable
- disable bootstrap admin in production
- override database credentials through environment variables
- replace local credential handoff with Notification Service integration
- the service normalizes its JVM timezone to UTC for consistent persistence and cross-environment behavior
