package com.school.user.repository;

import com.school.user.domain.TeacherProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeacherProfileRepository extends JpaRepository<TeacherProfile, UUID> {
    Optional<TeacherProfile> findByUserProfileId(UUID userProfileId);

    Optional<TeacherProfile> findByEmployeeCode(String employeeCode);
}
