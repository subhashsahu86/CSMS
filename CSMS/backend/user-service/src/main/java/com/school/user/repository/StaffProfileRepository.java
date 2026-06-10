package com.school.user.repository;

import com.school.user.domain.StaffProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StaffProfileRepository extends JpaRepository<StaffProfile, UUID> {
    Optional<StaffProfile> findByUserProfileId(UUID userProfileId);

    Optional<StaffProfile> findByEmployeeCode(String employeeCode);
}
