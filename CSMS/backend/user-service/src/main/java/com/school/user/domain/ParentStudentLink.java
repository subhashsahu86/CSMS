package com.school.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parent_student_links")
public class ParentStudentLink {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_user_profile_id", nullable = false)
    private UserProfile parentUserProfile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "student_user_profile_id", nullable = false)
    private UserProfile studentUserProfile;

    @Column(nullable = false, length = 50)
    private String relationship;

    @Column(name = "is_primary_guardian", nullable = false)
    private boolean primaryGuardian;

    @Column(name = "can_receive_notifications", nullable = false)
    private boolean canReceiveNotifications;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected ParentStudentLink() {
    }

    public ParentStudentLink(
            UUID id,
            UserProfile parentUserProfile,
            UserProfile studentUserProfile,
            String relationship,
            boolean primaryGuardian,
            boolean canReceiveNotifications
    ) {
        this.id = id;
        this.parentUserProfile = parentUserProfile;
        this.studentUserProfile = studentUserProfile;
        this.relationship = relationship;
        this.primaryGuardian = primaryGuardian;
        this.canReceiveNotifications = canReceiveNotifications;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UserProfile getParentUserProfile() {
        return parentUserProfile;
    }

    public UserProfile getStudentUserProfile() {
        return studentUserProfile;
    }

    public String getRelationship() {
        return relationship;
    }

    public boolean isPrimaryGuardian() {
        return primaryGuardian;
    }

    public boolean isCanReceiveNotifications() {
        return canReceiveNotifications;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
