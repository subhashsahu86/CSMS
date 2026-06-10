package com.school.user.repository;

import com.school.user.domain.ParentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ParentProfileRepository extends JpaRepository<ParentProfile, UUID> {
    Optional<ParentProfile> findByUserProfileId(UUID userProfileId);
}
