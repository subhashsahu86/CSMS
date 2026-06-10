package com.school.auth.domain;

public enum UserType {
    ADMIN("ADM"),
    STUDENT("STU"),
    TEACHER("TCH"),
    PARENT("PAR"),
    STAFF("STF");

    private final String prefix;

    UserType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
