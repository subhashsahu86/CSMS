package com.school.auth.service;

import com.school.auth.config.BootstrapAdminProperties;
import com.school.auth.domain.AccountStatus;
import com.school.auth.domain.AuthUser;
import com.school.auth.domain.Role;
import com.school.auth.exception.ResourceNotFoundException;
import com.school.auth.repository.AuthUserRepository;
import com.school.auth.repository.RoleRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public class BootstrapAdminInitializer implements ApplicationRunner {

    private final BootstrapAdminProperties properties;
    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public BootstrapAdminInitializer(
            BootstrapAdminProperties properties,
            AuthUserRepository authUserRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.properties = properties;
        this.authUserRepository = authUserRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!properties.enabled() || authUserRepository.existsByUsername(properties.username())) {
            return;
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new ResourceNotFoundException("role not found: ADMIN"));

        AuthUser admin = new AuthUser(
                UUID.randomUUID(),
                properties.username(),
                properties.email(),
                null,
                passwordEncoder.encode(properties.password()),
                AccountStatus.ACTIVE,
                true,
                "bootstrap"
        );
        admin.addRole(adminRole);
        authUserRepository.save(admin);
    }
}
