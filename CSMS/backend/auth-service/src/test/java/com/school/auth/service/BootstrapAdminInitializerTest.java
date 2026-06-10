package com.school.auth.service;

import com.school.auth.config.BootstrapAdminProperties;
import com.school.auth.domain.Role;
import com.school.auth.repository.AuthUserRepository;
import com.school.auth.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BootstrapAdminInitializerTest {

    @Test
    void createsBootstrapAdminWhenMissing() throws Exception {
        AuthUserRepository authUserRepository = mock(AuthUserRepository.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        BootstrapAdminProperties properties = new BootstrapAdminProperties(
                true,
                "ADM0001",
                "Admin@12345",
                "admin@school.local"
        );

        when(authUserRepository.existsByUsername("ADM0001")).thenReturn(false);
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(new Role(UUID.randomUUID(), "ADMIN")));

        BootstrapAdminInitializer initializer = new BootstrapAdminInitializer(
                properties,
                authUserRepository,
                roleRepository,
                new BCryptPasswordEncoder()
        );

        initializer.run(new DefaultApplicationArguments(new String[0]));

        verify(authUserRepository).save(any());
    }
}
