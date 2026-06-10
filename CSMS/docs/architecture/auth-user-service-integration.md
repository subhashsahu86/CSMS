# Auth Service + User Service Integration

## Purpose

This flow verifies the first cross-service business sequence:

1. Auth Service creates a login identity.
2. User Service creates the matching school profile.
3. User Service can retrieve the profile by `authUserId` and `username`.
4. The created student can log in with the temporary password from Auth Service.

## Local service ports

```text
Auth Service: http://localhost:8082
User Service: http://localhost:8083
```

## Start local databases

From the repository root:

```bash
docker compose -f infrastructure/docker/docker-compose.yml up -d
```

## Start Auth Service

From `backend/auth-service`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

## Start User Service

From `backend/user-service`:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

## Run the verification script

From the repository root:

```bash
bash scripts/verify-auth-user-student-flow.sh
```

Optional environment overrides:

```bash
AUTH_BASE_URL=http://localhost:8082 \
USER_BASE_URL=http://localhost:8083 \
ADMIN_USERNAME=ADM0001 \
ADMIN_PASSWORD=Admin@12345 \
bash scripts/verify-auth-user-student-flow.sh
```

## Expected result

The script should end with:

```text
SUCCESS: Auth Service + User Service student flow verified.
```

## Design note

User Service intentionally stores `auth_user_id` as a UUID value without a database foreign key to Auth Service. In a microservice architecture, Auth Service and User Service own separate databases. The relationship is enforced through service contracts, not cross-database foreign keys.
