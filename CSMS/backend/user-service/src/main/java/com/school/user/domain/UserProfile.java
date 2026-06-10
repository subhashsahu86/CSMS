package com.school.user.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    private UUID id;

    @Column(name = "auth_user_id", nullable = false, unique = true)
    private UUID authUserId;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 30)
    private UserType userType;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "primary_email")
    private String primaryEmail;

    @Column(name = "primary_phone", length = 30)
    private String primaryPhone;

    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ProfileStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses = new ArrayList<>();

    protected UserProfile() {
    }

    public UserProfile(
            UUID id,
            UUID authUserId,
            String username,
            UserType userType,
            String firstName,
            String middleName,
            String lastName,
            String displayName,
            Gender gender,
            LocalDate dateOfBirth,
            String primaryEmail,
            String primaryPhone,
            String profileImageUrl,
            ProfileStatus status
    ) {
        this.id = id;
        this.authUserId = authUserId;
        this.username = username;
        this.userType = userType;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.primaryEmail = primaryEmail;
        this.primaryPhone = primaryPhone;
        this.profileImageUrl = profileImageUrl;
        this.status = status;
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

    public UUID getAuthUserId() {
        return authUserId;
    }

    public String getUsername() {
        return username;
    }

    public UserType getUserType() {
        return userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Gender getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public ProfileStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<UserAddress> getAddresses() {
        return addresses;
    }

    public void addAddress(UserAddress address) {
        addresses.add(address);
        address.attachTo(this);
    }

    public void update(
            String firstName,
            String middleName,
            String lastName,
            String displayName,
            Gender gender,
            LocalDate dateOfBirth,
            String primaryEmail,
            String primaryPhone,
            String profileImageUrl,
            ProfileStatus status
    ) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.displayName = displayName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.primaryEmail = primaryEmail;
        this.primaryPhone = primaryPhone;
        this.profileImageUrl = profileImageUrl;
        this.status = status;
    }

    public void clearAddresses() {
        this.addresses.clear();
    }
}
