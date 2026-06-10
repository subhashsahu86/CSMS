package com.school.auth.domain;

import java.io.Serializable;
import java.util.Objects;

public class UsernameSequenceId implements Serializable {

    private UserType userType;
    private int admissionYear;

    public UsernameSequenceId() {
    }

    public UsernameSequenceId(UserType userType, int admissionYear) {
        this.userType = userType;
        this.admissionYear = admissionYear;
    }

    public UserType getUserType() {
        return userType;
    }

    public int getAdmissionYear() {
        return admissionYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsernameSequenceId that = (UsernameSequenceId) o;
        return admissionYear == that.admissionYear && userType == that.userType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userType, admissionYear);
    }
}
