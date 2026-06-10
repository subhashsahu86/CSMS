# Gateway Service

Single local entry point for backend services.

## Local port

```text
8080
```

## Routes

```text
/api/v1/auth/**      -> Auth Service 8082
/api/v1/admin/**     -> Auth Service 8082
/api/v1/profiles/**  -> User Service 8083
```

## Run locally

```bash
cd backend/gateway-service
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

## Health

```bash
curl http://localhost:8080/actuator/health
```
