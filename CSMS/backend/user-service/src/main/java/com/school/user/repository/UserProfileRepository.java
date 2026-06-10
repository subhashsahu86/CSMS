package com.school.user.repository;

import com.school.user.domain.UserProfile;
import com.school.user.domain.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByAuthUserId(UUID authUserId);

    Optional<UserProfile> findByUsername(String username);

    boolean existsByAuthUserId(UUID authUserId);

    boolean existsByUsername(String username);

    List<UserProfile> findByUserType(UserType userType);
}
