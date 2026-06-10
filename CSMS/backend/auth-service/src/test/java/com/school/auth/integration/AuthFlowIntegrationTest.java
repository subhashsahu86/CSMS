package com.school.auth.integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.school.auth.domain.UserType;
import com.school.auth.dto.request.ChangePasswordRequest;
import com.school.auth.dto.request.CreateUserRequest;
import com.school.auth.dto.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthFlowIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("auth_db")
            .withUsername("auth_user")
            .withPassword("auth_password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> postgres.getJdbcUrl() + "&options=-c%20TimeZone=UTC");
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("security.jwt.secret", () -> "test-secret-test-secret-test-secret-test-secret");
        registry.add("security.bootstrap-admin.enabled", () -> true);
        registry.add("security.bootstrap-admin.username", () -> "ADM0001");
        registry.add("security.bootstrap-admin.password", () -> "Admin@12345");
        registry.add("security.bootstrap-admin.email", () -> "admin@school.local");
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullAuthFlowWorksAgainstPostgres() throws Exception {
        ResponseEntity<String> adminLogin = restTemplate.postForEntity(
                url("/api/v1/auth/login"),
                new LoginRequest("ADM0001", "Admin@12345"),
                String.class
        );
        assertThat(adminLogin.getStatusCode().is2xxSuccessful()).isTrue();
        String adminAccessToken = objectMapper.readTree(adminLogin.getBody()).get("accessToken").asText();

        HttpHeaders adminHeaders = bearerHeaders(adminAccessToken);
        ResponseEntity<String> createStudent = restTemplate.exchange(
                url("/api/v1/admin/users"),
                HttpMethod.POST,
                new HttpEntity<>(
                        new CreateUserRequest(UserType.STUDENT, 2026, null, "9876543210", "ADM0001"),
                        adminHeaders
                ),
                String.class
        );
        assertThat(createStudent.getStatusCode().value()).isEqualTo(201);

        JsonNode createdStudent = objectMapper.readTree(createStudent.getBody());
        String studentUsername = createdStudent.get("username").asText();
        String temporaryPassword = createdStudent.get("temporaryPassword").asText();

        ResponseEntity<String> studentLogin = restTemplate.postForEntity(
                url("/api/v1/auth/login"),
                new LoginRequest(studentUsername, temporaryPassword),
                String.class
        );
        assertThat(studentLogin.getStatusCode().is2xxSuccessful()).isTrue();
        JsonNode loginPayload = objectMapper.readTree(studentLogin.getBody());
        assertThat(loginPayload.get("mustChangePassword").asBoolean()).isTrue();

        String studentAccessToken = loginPayload.get("accessToken").asText();
        ResponseEntity<Void> changePassword = restTemplate.exchange(
                url("/api/v1/auth/change-password"),
                HttpMethod.POST,
                new HttpEntity<>(
                        new ChangePasswordRequest(temporaryPassword, "New@12345"),
                        bearerHeaders(studentAccessToken)
                ),
                Void.class
        );
        assertThat(changePassword.getStatusCode().value()).isEqualTo(204);
    }

    private HttpHeaders bearerHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }
}
