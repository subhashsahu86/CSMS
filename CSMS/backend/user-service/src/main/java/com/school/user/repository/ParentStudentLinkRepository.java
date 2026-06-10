package com.school.user.repository;

import com.school.user.domain.ParentStudentLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ParentStudentLinkRepository extends JpaRepository<ParentStudentLink, UUID> {
    List<ParentStudentLink> findByParentUserProfileId(UUID parentUserProfileId);

    List<ParentStudentLink> findByStudentUserProfileId(UUID studentUserProfileId);
}
