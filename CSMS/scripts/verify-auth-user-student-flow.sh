#!/usr/bin/env bash
set -euo pipefail

AUTH_BASE_URL="${AUTH_BASE_URL:-http://localhost:8082}"
USER_BASE_URL="${USER_BASE_URL:-http://localhost:8083}"
ADMIN_USERNAME="${ADMIN_USERNAME:-ADM0001}"
ADMIN_PASSWORD="${ADMIN_PASSWORD:-Admin@12345}"

require_command() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Required command not found: $1" >&2
    exit 1
  fi
}

require_command curl
require_command python3

json_get() {
  python3 -c 'import json,sys; data=json.load(sys.stdin); print(data["'"$1"'"])'
}

json_get_nested() {
  python3 -c 'import json,sys; data=json.load(sys.stdin); cur=data; 
for key in "'"$1"'".split("."):
    cur = cur[key]
print(cur)'
}

post_json() {
  local url="$1"
  local body="$2"
  shift 2
  curl --fail-with-body --silent --show-error \
    -H "Content-Type: application/json" \
    "$@" \
    -X POST "$url" \
    -d "$body"
}

get_json() {
  local url="$1"
  shift
  curl --fail-with-body --silent --show-error \
    -H "Content-Type: application/json" \
    "$@" \
    "$url"
}

echo "Checking service health..."
get_json "$AUTH_BASE_URL/actuator/health" >/dev/null
get_json "$USER_BASE_URL/actuator/health" >/dev/null

echo "Logging in as bootstrap admin: $ADMIN_USERNAME"
ADMIN_LOGIN_RESPONSE="$(post_json "$AUTH_BASE_URL/api/v1/auth/login" \
  "{\"username\":\"$ADMIN_USERNAME\",\"password\":\"$ADMIN_PASSWORD\"}")"
ADMIN_ACCESS_TOKEN="$(printf '%s' "$ADMIN_LOGIN_RESPONSE" | json_get accessToken)"

echo "Creating STUDENT auth account..."
CREATE_AUTH_RESPONSE="$(post_json "$AUTH_BASE_URL/api/v1/admin/users" \
  '{"userType":"STUDENT","admissionYear":2026,"email":null,"phoneNumber":"9876543210","createdBy":"integration-script"}' \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN")"

AUTH_USER_ID="$(printf '%s' "$CREATE_AUTH_RESPONSE" | json_get userId)"
USERNAME="$(printf '%s' "$CREATE_AUTH_RESPONSE" | json_get username)"
TEMP_PASSWORD="$(printf '%s' "$CREATE_AUTH_RESPONSE" | json_get temporaryPassword)"

echo "Created auth user:"
echo "  authUserId: $AUTH_USER_ID"
echo "  username:   $USERNAME"
echo "  temporary password generated: yes"

echo "Creating matching User Service student profile..."
CREATE_PROFILE_RESPONSE="$(post_json "$USER_BASE_URL/api/v1/profiles/students" \
  "{
    \"authUserId\":\"$AUTH_USER_ID\",
    \"username\":\"$USERNAME\",
    \"firstName\":\"Integration\",
    \"middleName\":null,
    \"lastName\":\"Student\",
    \"displayName\":\"Integration Student\",
    \"gender\":\"NOT_SPECIFIED\",
    \"dateOfBirth\":\"2012-04-12\",
    \"primaryEmail\":null,
    \"primaryPhone\":\"9876543210\",
    \"profileImageUrl\":null,
    \"registrationNumber\":\"REG-$USERNAME\",
    \"admissionNumber\":\"ADM-$USERNAME\",
    \"admissionDate\":\"2026-06-01\",
    \"bloodGroup\":\"O+\",
    \"category\":\"General\",
    \"previousSchool\":null,
    \"addresses\":[
      {
        \"addressType\":\"RESIDENTIAL\",
        \"addressLine1\":\"123 Integration Street\",
        \"addressLine2\":null,
        \"city\":\"Mumbai\",
        \"state\":\"Maharashtra\",
        \"postalCode\":\"400001\",
        \"country\":\"India\"
      }
    ]
  }" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN")"

PROFILE_ID="$(printf '%s' "$CREATE_PROFILE_RESPONSE" | json_get id)"

echo "Created user profile:"
echo "  profileId: $PROFILE_ID"

echo "Verifying lookup by authUserId..."
BY_AUTH_RESPONSE="$(get_json "$USER_BASE_URL/api/v1/profiles/by-auth-user/$AUTH_USER_ID" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN")"
BY_AUTH_USERNAME="$(printf '%s' "$BY_AUTH_RESPONSE" | json_get username)"

echo "Verifying lookup by username..."
BY_USERNAME_RESPONSE="$(get_json "$USER_BASE_URL/api/v1/profiles/by-username/$USERNAME" \
  -H "Authorization: Bearer $ADMIN_ACCESS_TOKEN")"
BY_USERNAME_AUTH_ID="$(printf '%s' "$BY_USERNAME_RESPONSE" | json_get authUserId)"

if [[ "$BY_AUTH_USERNAME" != "$USERNAME" ]]; then
  echo "Lookup by authUserId returned unexpected username: $BY_AUTH_USERNAME" >&2
  exit 1
fi

if [[ "$BY_USERNAME_AUTH_ID" != "$AUTH_USER_ID" ]]; then
  echo "Lookup by username returned unexpected authUserId: $BY_USERNAME_AUTH_ID" >&2
  exit 1
fi

echo "Verifying student can login with temporary password..."
STUDENT_LOGIN_RESPONSE="$(post_json "$AUTH_BASE_URL/api/v1/auth/login" \
  "{\"username\":\"$USERNAME\",\"password\":\"$TEMP_PASSWORD\"}")"
MUST_CHANGE_PASSWORD="$(printf '%s' "$STUDENT_LOGIN_RESPONSE" | json_get mustChangePassword)"

if [[ "$MUST_CHANGE_PASSWORD" != "True" && "$MUST_CHANGE_PASSWORD" != "true" ]]; then
  echo "Expected mustChangePassword=true but got: $MUST_CHANGE_PASSWORD" >&2
  exit 1
fi

echo
echo "SUCCESS: Auth Service + User Service student flow verified."
echo "  username: $USERNAME"
echo "  authUserId: $AUTH_USER_ID"
echo "  profileId: $PROFILE_ID"
