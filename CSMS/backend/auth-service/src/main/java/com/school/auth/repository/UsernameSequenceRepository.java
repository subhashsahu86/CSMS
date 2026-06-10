package com.school.auth.repository;

import com.school.auth.domain.UserType;
import com.school.auth.domain.UsernameSequence;
import com.school.auth.domain.UsernameSequenceId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UsernameSequenceRepository extends JpaRepository<UsernameSequence, UsernameSequenceId> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select s
            from UsernameSequence s
            where s.userType = :userType and s.admissionYear = :admissionYear
            """)
    Optional<UsernameSequence> findForUpdate(
            @Param("userType") UserType userType,
            @Param("admissionYear") int admissionYear
    );
}
