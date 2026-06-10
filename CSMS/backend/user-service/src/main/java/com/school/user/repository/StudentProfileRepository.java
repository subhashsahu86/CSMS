package com.school.user.repository;

import com.school.user.domain.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, UUID> {
    Optional<StudentProfile> findByUserProfileId(UUID userProfileId);

    Optional<StudentProfile> findByRegistrationNumber(String registrationNumber);

    Optional<StudentProfile> findByAdmissionNumber(String admissionNumber);
}
