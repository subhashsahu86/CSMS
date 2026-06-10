package com.school.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.bootstrap-admin")
public record BootstrapAdminProperties(
        boolean enabled,
        String username,
        String password,
        String email
) {
}
