package com.granitebayace.site.objects;

public record UserData(String username, String passhash, Role role) {
    @Override
    public String toString() {
        return String.format("{name: %s, hash: %s, role: %s}", username, passhash, role.toString());
    }
}
