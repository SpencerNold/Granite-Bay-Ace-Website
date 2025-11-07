package com.granitebayace.site.objects;

public record UserData(String username, String passhash, Session session, Role role) {
    @Override
    public String toString() {
        return String.format("{name: %s, hash: %s, session: %s, role: %s}", username, passhash, String.valueOf(session), String.valueOf(role));
    }
}
