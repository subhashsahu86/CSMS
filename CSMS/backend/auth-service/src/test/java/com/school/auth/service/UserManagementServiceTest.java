package com.school.auth.service;

import com.school.auth.domain.AuthUser;
import com.school.auth.domain.Role;
import com.school.auth.domain.UserType;
import com.school.auth.dto.request.CreateUserRequest;
import com.school.auth.dto.response.CreateUserResponse;
import com.school.auth.exception.DuplicateResourceException;
import com.school.auth.repository.AuthUserRepository;
import com.school.auth.repository.RoleRepository;
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

class UserManagementServiceTest {

    private AuthUserRepository authUserRepository;
    private RoleRepository roleRepository;
    private UsernameGeneratorService usernameGeneratorService;
    private TemporaryPasswordGenerator temporaryPasswordGenerator;
    private PasswordEncoder passwordEncoder;
    private UserManagementService userManagementService;

    @BeforeEach
    void setUp() {
        authUserRepository = mock(AuthUserRepository.class);
        roleRepository = mock(RoleRepository.class);
        usernameGeneratorService = mock(UsernameGeneratorService.class);
        temporaryPasswordGenerator = mock(TemporaryPasswordGenerator.class);
        passwordEncoder = new BCryptPasswordEncoder();
        userManagementService = new UserManagementService(
                authUserRepository,
                roleRepository,
                usernameGeneratorService,
                temporaryPasswordGenerator,
                passwordEncoder
        );
    }

    @Test
    void createsUserWithGeneratedUsernameTemporaryPasswordAndRole() {
        CreateUserRequest request = new CreateUserRequest(
                UserType.STUDENT,
                2026,
                null,
                "9876543210",
                "system"
        );
        Role studentRole = new Role(UUID.randomUUID(), "STUDENT");

        when(usernameGeneratorService.generate(UserType.STUDENT, 2026)).thenReturn("STU2026000001");
        when(temporaryPasswordGenerator.generate()).thenReturn("Abc@1234XY");
        when(roleRepository.findByName("STUDENT")).thenReturn(Optional.of(studentRole));
        when(authUserRepository.save(any(AuthUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CreateUserResponse response = userManagementService.createUser(request);

        assertThat(response.username()).isEqualTo("STU2026000001");
        assertThat(response.temporaryPassword()).isEqualTo("Abc@1234XY");
        assertThat(response.mustChangePassword()).isTrue();
        assertThat(response.roles()).containsExactly("STUDENT");
    }

    @Test
    void rejectsDuplicateEmail() {
        CreateUserRequest request = new CreateUserRequest(
                UserType.TEACHER,
                2026,
                "teacher@school.com",
                null,
                "admin"
        );

        when(authUserRepository.existsByEmail("teacher@school.com")).thenReturn(true);

        assertThatThrownBy(() -> userManagementService.createUser(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already exists");
    }
}
