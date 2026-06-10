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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_profiles")
public class StudentProfile {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    private UserProfile userProfile;

    @Column(name = "registration_number", unique = true, length = 50)
    private String registrationNumber;

    @Column(name = "admission_number", unique = true, length = 50)
    private String admissionNumber;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "blood_group", length = 10)
    private String bloodGroup;

    @Column(length = 50)
    private String category;

    @Column(name = "previous_school")
    private String previousSchool;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected StudentProfile() {
    }

    public StudentProfile(
            UUID id,
            UserProfile userProfile,
            String registrationNumber,
            String admissionNumber,
            LocalDate admissionDate,
            String bloodGroup,
            String category,
            String previousSchool
    ) {
        this.id = id;
        this.userProfile = userProfile;
        this.registrationNumber = registrationNumber;
        this.admissionNumber = admissionNumber;
        this.admissionDate = admissionDate;
        this.bloodGroup = bloodGroup;
        this.category = category;
        this.previousSchool = previousSchool;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getAdmissionNumber() {
        return admissionNumber;
    }

    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getCategory() {
        return category;
    }

    public String getPreviousSchool() {
        return previousSchool;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(
            String registrationNumber,
            String admissionNumber,
            LocalDate admissionDate,
            String bloodGroup,
            String category,
            String previousSchool
    ) {
        this.registrationNumber = registrationNumber;
        this.admissionNumber = admissionNumber;
        this.admissionDate = admissionDate;
        this.bloodGroup = bloodGroup;
        this.category = category;
        this.previousSchool = previousSchool;
    }
}
