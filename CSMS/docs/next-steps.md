# Next Steps

## Immediate Next Step

### CI/CD — Step 2

Goal:
- add quality gates and prepare deployment manifests.

Recommended:
- add JaCoCo coverage
- add Checkstyle/Spotless formatting
- add Docker Compose deployment file using built images
- later add Helm charts

## Completed

### CI/CD — Step 1
- GitHub Actions CI test workflow
- Docker image build/push workflow
- GHCR-ready image tags
- CI/CD documentation

### Auth Service
- username login
- generated usernames
- JWT/refresh/logout/password change
- admin user creation

### User Service
- student/teacher/parent/staff profiles
- JWT validation
- profile lookup APIs

### Gateway Service
- routes Auth/User/Admission APIs

### Admission Service — Step 1
- service skeleton
- Postgres config
- Swagger/OpenAPI
- Dockerfile
- tests passing

## Later Phase 1

1. Admission workflow completion
2. Academic base service
3. Frontend foundation
