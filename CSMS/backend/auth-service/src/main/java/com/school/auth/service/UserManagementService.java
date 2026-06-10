package com.school.auth.service;

import com.school.auth.domain.AccountStatus;
import com.school.auth.domain.AuthUser;
import com.school.auth.domain.Role;
import com.school.auth.domain.UserType;
import com.school.auth.dto.request.CreateUserRequest;
import com.school.auth.dto.response.CreateUserResponse;
import com.school.auth.exception.DuplicateResourceException;
import com.school.auth.exception.ResourceNotFoundException;
import com.school.auth.repository.AuthUserRepository;
import com.school.auth.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class UserManagementService {

    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;
    private final UsernameGeneratorService usernameGeneratorService;
    private final TemporaryPasswordGenerator temporaryPasswordGenerator;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(
            AuthUserRepository authUserRepository,
            RoleRepository roleRepository,
            UsernameGeneratorService usernameGeneratorService,
            TemporaryPasswordGenerator temporaryPasswordGenerator,
            PasswordEncoder passwordEncoder
    ) {
        this.authUserRepository = authUserRepository;
        this.roleRepository = roleRepository;
        this.usernameGeneratorService = usernameGeneratorService;
        this.temporaryPasswordGenerator = temporaryPasswordGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public CreateUserResponse createUser(CreateUserRequest request) {
        if (request.email() != null && authUserRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("email already exists");
        }

        String username = usernameGeneratorService.generate(request.userType(), request.admissionYear());
        String temporaryPassword = temporaryPasswordGenerator.generate();
        Role role = findRoleFor(request.userType());

        AuthUser authUser = new AuthUser(
                UUID.randomUUID(),
                username,
                request.email(),
                request.phoneNumber(),
                passwordEncoder.encode(temporaryPassword),
                AccountStatus.ACTIVE,
                true,
                request.createdBy()
        );
        authUser.addRole(role);

        AuthUser savedUser = authUserRepository.save(authUser);

        return new CreateUserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                temporaryPassword,
                savedUser.isMustChangePassword(),
                Set.of(role.getName())
        );
    }

    private Role findRoleFor(UserType userType) {
        return roleRepository.findByName(userType.name())
                .orElseThrow(() -> new ResourceNotFoundException("role not found: " + userType.name()));
    }
}
