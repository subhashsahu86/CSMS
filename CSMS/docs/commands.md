# Common Commands

## Start databases

From project root:

```bash
docker compose -f infrastructure/docker/docker-compose.yml up -d
```

## Run Auth Service

```bash
cd backend/auth-service
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

## Run User Service

```bash
cd backend/user-service
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

## Run Gateway Service

```bash
cd backend/gateway-service
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

## Run Admission Service

```bash
cd backend/admission-service
mvn spring-boot:run -Dspring-boot.run.profiles=local -Dspring-boot.run.jvmArguments="-Duser.timezone=Asia/Kolkata"
```

## Test Auth Service

```bash
cd backend/auth-service
mvn test
```

## Test User Service

```bash
cd backend/user-service
mvn test
```

## Test Gateway Service

```bash
cd backend/gateway-service
mvn test
```

## Test Admission Service

```bash
cd backend/admission-service
mvn test
```

## Verify Auth + User integration

From project root:

```bash
bash scripts/verify-auth-user-student-flow.sh
```

## Save Maven logs for debugging

```bash
mvn test > test-output.log 2>&1
```
