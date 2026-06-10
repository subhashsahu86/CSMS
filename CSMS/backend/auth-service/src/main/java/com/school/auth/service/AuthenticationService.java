package com.school.auth.service;

import com.school.auth.domain.AccountStatus;
import com.school.auth.domain.AuthUser;
import com.school.auth.dto.request.LoginRequest;
import com.school.auth.dto.request.ChangePasswordRequest;
import com.school.auth.dto.response.AuthUserSummary;
import com.school.auth.dto.response.LoginResponse;
import com.school.auth.dto.response.TokenResponse;
import com.school.auth.exception.InvalidCredentialsException;
import com.school.auth.repository.AuthUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class AuthenticationService {

    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    public AuthenticationService(
            AuthUserRepository authUserRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService
    ) {
        this.authUserRepository = authUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public LoginResponse login(LoginRequest request, String deviceInfo, String ipAddress) {
        AuthUser user = authUserRepository.findByUsername(request.username())
                .orElseThrow(() -> new InvalidCredentialsException("invalid username or password"));

        if (user.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidCredentialsException("account is not active");
        }

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            user.recordFailedLogin();
            if (user.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
                user.lock();
            }
            throw new InvalidCredentialsException("invalid username or password");
        }

        user.recordSuccessfulLogin();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.issue(user, deviceInfo, ipAddress);

        return new LoginResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.accessTokenExpiresInSeconds(),
                user.isMustChangePassword(),
                toSummary(user)
        );
    }

    @Transactional(readOnly = true)
    public TokenResponse refresh(String rawRefreshToken) {
        AuthUser user = refreshTokenService.validate(rawRefreshToken).getUser();
        String accessToken = jwtService.generateAccessToken(user);

        return new TokenResponse(
                accessToken,
                "Bearer",
                jwtService.accessTokenExpiresInSeconds()
        );
    }

    @Transactional(readOnly = true)
    public AuthUserSummary currentUser(String username) {
        AuthUser user = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("user not found"));
        return toSummary(user);
    }

    @Transactional
    public void changePassword(String username, ChangePasswordRequest request) {
        AuthUser user = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("user not found"));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new InvalidCredentialsException("current password is invalid");
        }

        user.updatePasswordHash(passwordEncoder.encode(request.newPassword()));
        user.markPasswordChanged();
    }

    public void logout(String rawRefreshToken) {
        refreshTokenService.revoke(rawRefreshToken);
    }

    private AuthUserSummary toSummary(AuthUser user) {
        Set<String> roles = user.getRoles().stream()
                .map(role -> role.getName())
                .collect(java.util.stream.Collectors.toSet());

        return new AuthUserSummary(user.getId(), user.getUsername(), roles);
    }
}
