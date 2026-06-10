# User Service

## Boundary

User Service owns school-person profile data. It does not store passwords, refresh tokens, permissions, or login state.

## Relationship with Auth Service

Auth Service creates identities such as:

```text
auth_user_id: UUID
username: STU2026000001
role: STUDENT
```

User Service creates the corresponding profile:

```text
student name
date of birth
phone/email
address
guardian links
student metadata
```

The services are intentionally linked by `auth_user_id` and `username`, but User Service does not use foreign keys into Auth Service because each microservice owns its database.

## Profile model

The schema uses:

- `user_profiles` for common fields
- role-specific extension tables:
  - `student_profiles`
  - `teacher_profiles`
  - `parent_profiles`
  - `staff_profiles`
- `user_addresses`
- `parent_student_links`
