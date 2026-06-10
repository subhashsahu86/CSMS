package com.school.auth.controller;

import com.school.auth.dto.request.LoginRequest;
import com.school.auth.dto.request.ChangePasswordRequest;
import com.school.auth.dto.request.LogoutRequest;
import com.school.auth.dto.request.RefreshTokenRequest;
import com.school.auth.dto.response.AuthUserSummary;
import com.school.auth.dto.response.LoginResponse;
import com.school.auth.dto.response.TokenResponse;
import com.school.auth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpServletRequest
    ) {
        String deviceInfo = httpServletRequest.getHeader("User-Agent");
        String ipAddress = httpServletRequest.getRemoteAddr();
        return ResponseEntity.ok(authenticationService.login(request, deviceInfo, ipAddress));
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationService.refresh(request.refreshToken()));
    }

    @GetMapping("/me")
    public ResponseEntity<AuthUserSummary> me(Authentication authentication) {
        return ResponseEntity.ok(authenticationService.currentUser(authentication.getName()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        authenticationService.changePassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        authenticationService.logout(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
