package com.granitebayace.site.objects;

public record UserData(String username, String passhash, String salt, Session session, Role role) {
    @Override
    public String toString() {
        return String.format("{name: %s, hash: %s, salt: %s, session: %s, role: %s}", username, passhash, salt, String.valueOf(session), String.valueOf(role));
    }
}
