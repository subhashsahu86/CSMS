package com.school.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "username_sequences")
@IdClass(UsernameSequenceId.class)
public class UsernameSequence {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false, length = 20)
    private UserType userType;

    @Id
    @Column(name = "admission_year", nullable = false)
    private int admissionYear;

    @Column(name = "next_value", nullable = false)
    private long nextValue;

    protected UsernameSequence() {
    }

    public UsernameSequence(UserType userType, int admissionYear, long nextValue) {
        this.userType = userType;
        this.admissionYear = admissionYear;
        this.nextValue = nextValue;
    }

    public UserType getUserType() {
        return userType;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    public long getNextValue() {
        return nextValue;
    }

    public long nextAndIncrement() {
        long currentValue = nextValue;
        nextValue++;
        return currentValue;
    }
}
