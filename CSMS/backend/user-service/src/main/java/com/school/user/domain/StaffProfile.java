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
@Table(name = "staff_profiles")
public class StaffProfile {

    @Id
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_profile_id", nullable = false, unique = true)
    private UserProfile userProfile;

    @Column(name = "employee_code", unique = true, length = 50)
    private String employeeCode;

    @Column(length = 100)
    private String department;

    @Column(length = 100)
    private String designation;

    @Column(name = "joining_date")
    private LocalDate joiningDate;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected StaffProfile() {
    }

    public StaffProfile(UUID id, UserProfile userProfile, String employeeCode, String department, String designation, LocalDate joiningDate) {
        this.id = id;
        this.userProfile = userProfile;
        this.employeeCode = employeeCode;
        this.department = department;
        this.designation = designation;
        this.joiningDate = joiningDate;
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

    public String getDepartment() {
        return department;
    }

    public String getDesignation() {
        return designation;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void update(
            String employeeCode,
            String department,
            String designation,
            LocalDate joiningDate
    ) {
        this.employeeCode = employeeCode;
        this.department = department;
        this.designation = designation;
        this.joiningDate = joiningDate;
    }
}
