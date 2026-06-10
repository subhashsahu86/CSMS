package com.school.auth.service;

import com.school.auth.domain.AccountStatus;
import com.school.auth.domain.AuthUser;
import com.school.auth.domain.Role;
import com.school.auth.dto.request.LoginRequest;
import com.school.auth.dto.request.ChangePasswordRequest;
import com.school.auth.dto.response.LoginResponse;
import com.school.auth.exception.InvalidCredentialsException;
import com.school.auth.repository.AuthUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    private AuthUserRepository authUserRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;
    private RefreshTokenService refreshTokenService;
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authUserRepository = mock(AuthUserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        jwtService = mock(JwtService.class);
        refreshTokenService = mock(RefreshTokenService.class);
        authenticationService = new AuthenticationService(
                authUserRepository,
                passwordEncoder,
                jwtService,
                refreshTokenService
        );
    }

    @Test
    void logsInActiveUserWithValidPassword() {
        AuthUser user = new AuthUser(
                UUID.randomUUID(),
                "STU2026000001",
                null,
                "9876543210",
                passwordEncoder.encode("Valid@1234"),
                AccountStatus.ACTIVE,
                true,
                "system"
        );
        user.addRole(new Role(UUID.randomUUID(), "STUDENT"));

        when(authUserRepository.findByUsername("STU2026000001")).thenReturn(Optional.of(user));
        when(jwtService.generateAccessToken(user)).thenReturn("access-token");
        when(jwtService.accessTokenExpiresInSeconds()).thenReturn(900L);
        when(refreshTokenService.issue(any(), any(), any())).thenReturn("refresh-token");

        LoginResponse response = authenticationService.login(
                new LoginRequest("STU2026000001", "Valid@1234"),
                "JUnit",
                "127.0.0.1"
        );

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        assertThat(response.mustChangePassword()).isTrue();
        assertThat(response.user().roles()).containsExactly("STUDENT");
    }

    @Test
    void rejectsInvalidPassword() {
        AuthUser user = new AuthUser(
                UUID.randomUUID(),
                "STU2026000001",
                null,
                "9876543210",
                passwordEncoder.encode("Valid@1234"),
                AccountStatus.ACTIVE,
                true,
                "system"
        );

        when(authUserRepository.findByUsername("STU2026000001")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authenticationService.login(
                new LoginRequest("STU2026000001", "wrong-password"),
                "JUnit",
                "127.0.0.1"
        )).isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("invalid username or password");
    }

    @Test
    void changesPasswordAndClearsMustChangePasswordFlag() {
        AuthUser user = new AuthUser(
                UUID.randomUUID(),
                "STU2026000001",
                null,
                "9876543210",
                passwordEncoder.encode("Old@12345"),
                AccountStatus.ACTIVE,
                true,
                "system"
        );

        when(authUserRepository.findByUsername("STU2026000001")).thenReturn(Optional.of(user));

        authenticationService.changePassword(
                "STU2026000001",
                new ChangePasswordRequest("Old@12345", "New@12345")
        );

        assertThat(user.isMustChangePassword()).isFalse();
        assertThat(passwordEncoder.matches("New@12345", user.getPasswordHash())).isTrue();
    }

    @Test
    void locksAccountAfterFiveFailedAttempts() {
        AuthUser user = new AuthUser(
                UUID.randomUUID(),
                "STU2026000001",
                null,
                "9876543210",
                passwordEncoder.encode("Valid@1234"),
                AccountStatus.ACTIVE,
                true,
                "system"
        );

        when(authUserRepository.findByUsername("STU2026000001")).thenReturn(Optional.of(user));

        for (int i = 0; i < 5; i++) {
            assertThatThrownBy(() -> authenticationService.login(
                    new LoginRequest("STU2026000001", "wrong-password"),
                    "JUnit",
                    "127.0.0.1"
            )).isInstanceOf(InvalidCredentialsException.class);
        }

        assertThat(user.getAccountStatus()).isEqualTo(AccountStatus.LOCKED);
    }
}
