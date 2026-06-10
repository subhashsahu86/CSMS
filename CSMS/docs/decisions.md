# Architecture Decisions

## Frontend

- Use React with Next.js.
- Start with one role-based app, split later only if needed.

## Backend

- Java 21.
- Spring Boot microservices.
- PostgreSQL per service.
- Docker Compose for local development.

## Service Ownership

### Auth Service owns

- credentials
- username
- password hash
- roles
- JWT
- refresh tokens
- login/logout
- password change

### User Service owns

- common person profile
- student profile
- teacher profile
- parent profile
- staff profile
- addresses
- parent-student relationships

## Username Strategy

Student username:

```text
STU + admissionYear + 6-digit sequence
Example: STU2026000001
```

## Service Communication

Current state:
- Auth Service does not call User Service.
- User Service does not call Auth Service.
- Integration is verified through script/client.

Target state:
- Admission Service will orchestrate Auth + User + Notification.
- Use Feign clients when Admission Service is built.

## Security Direction

Current security:
- User Service validates JWTs issued by Auth Service using the same HMAC secret.

Next security step:
- API Gateway will enforce routing and shared auth concerns.
