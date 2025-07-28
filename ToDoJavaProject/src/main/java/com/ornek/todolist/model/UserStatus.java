package com.ornek.todolist.model;

/**
 * Kullanıcı durumu enum'u
 */
public enum UserStatus {
    ONLINE("Çevrimiçi"),
    AWAY("Uzakta"),
    DO_NOT_DISTURB("Rahatsız Etmeyin");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 