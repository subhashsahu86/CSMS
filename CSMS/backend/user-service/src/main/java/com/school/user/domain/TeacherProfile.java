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
@Table(name = "teacher_profiles")
public class TeacherProfile {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    private UserProfile userProfile;

    @Column(name = "employee_code", unique = true, length = 50)
    private String employeeCode;

    private String qualification;

    private String specialization;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected TeacherProfile() {
    }

    public TeacherProfile(
            UUID id,
            UserProfile userProfile,
            String employeeCode,
            String qualification,
            String specialization,
            LocalDate joiningDate,
            Integer experienceYears
    ) {
        this.id = id;
        this.userProfile = userProfile;
        this.employeeCode = employeeCode;
        this.qualification = qualification;
        this.specialization = specialization;
        this.joiningDate = joiningDate;
        this.experienceYears = experienceYears;
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

    public String getEmployeeCode() {
        return employeeCode;
    }

    public String getQualification() {
        return qualification;
    }

    public String getSpecialization() {
        return specialization;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public Integer getExperienceYears() {
        return experienceYears;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(
            String employeeCode,
            String qualification,
            String specialization,
            LocalDate joiningDate,
            Integer experienceYears
    ) {
        this.employeeCode = employeeCode;
        this.qualification = qualification;
        this.specialization = specialization;
        this.joiningDate = joiningDate;
        this.experienceYears = experienceYears;
    }
}
