# Auth Service

## Scope

The Auth Service owns authentication and authorization concerns only:

- username-based login
- password hashing
- JWT access tokens
- refresh tokens
- account status
- role assignment
- forced password change on first login

Profile details remain outside this service and will belong to User Service.

## Username strategy

- Student username format: `STU + admissionYear + 6-digit sequence`
- Example: `STU2026000123`
- Username is the primary login identifier.
- Email and phone are optional contact channels.

## Credential delivery

When a student account is created, the platform should generate a temporary password, store only its hash, and deliver the username and temporary password through available contact channels:

1. student mobile
2. student email
3. parent mobile
4. parent email

The user must change the temporary password on first login.

## Future integration

Credential delivery should eventually be handled by Notification Service. Auth Service should create the account and expose or emit the minimal information required for secure one-time delivery without storing plain-text passwords.
