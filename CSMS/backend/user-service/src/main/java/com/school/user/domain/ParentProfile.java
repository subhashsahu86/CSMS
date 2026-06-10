package com.school.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parent_profiles")
public class ParentProfile {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    private UserProfile userProfile;

    private String occupation;

    @Column(name = "relationship_label", length = 50)
    private String relationshipLabel;

    @Column(name = "alternate_phone", length = 30)
    private String alternatePhone;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected ParentProfile() {
    }

    public ParentProfile(UUID id, UserProfile userProfile, String occupation, String relationshipLabel, String alternatePhone) {
        this.id = id;
        this.userProfile = userProfile;
        this.occupation = occupation;
        this.relationshipLabel = relationshipLabel;
        this.alternatePhone = alternatePhone;
    }

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getRelationshipLabel() {
        return relationshipLabel;
    }

    public String getAlternatePhone() {
        return alternatePhone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(
            String occupation,
            String relationshipLabel,
            String alternatePhone
    ) {
        this.occupation = occupation;
        this.relationshipLabel = relationshipLabel;
        this.alternatePhone = alternatePhone;
    }
}
