package com.example.jwt.domain;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String role() {
        return role;
    }


}
