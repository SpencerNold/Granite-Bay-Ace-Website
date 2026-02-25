package com.granitebayace.site.objects;

import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.SessionManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Handles password hashing (SHA3-256), validation, and session generation.
 */
public final class Hashing {

    private static final int SALT_BYTES = 16;

    private Hashing() {}

    // ─────────────────────────────────────────────
    // 1) HASHING FOR STORAGE (SIGNUP)
    // ─────────────────────────────────────────────
    public static String generateSalt() {
        byte[] salt = new byte[SALT_BYTES];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash password with SHA3-256 using a per-user Base64 salt.
     * Returns Base64 encoded digest.
     */
    public static String hashForStorage(String saltB64, String plainPassword) {
        try {
            byte[] salt = Base64.getDecoder().decode(saltB64);
            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            md.update(salt);
            md.update(plainPassword.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            return Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }

    // ─────────────────────────────────────────────
    // 2) LOGIN FLOW
    // ─────────────────────────────────────────────
    public static AuthResult login(DatabaseLayer db, String username, String plainPassword, long daysValid) {
        UserData user = db.queryUserData(username);
        if (user == null) {
            // mimic timing if user not found
            fakeDelay(plainPassword);
            return AuthResult.failure("Invalid credentials");
        }

        String recompute = hashForStorage(user.salt(), plainPassword);
        boolean match = constantTimeEquals(recompute, user.passhash());
        if (!match) {
            return AuthResult.failure("Invalid credentials");
        }

        //reuse existing sesion
        Session session = user.session();
        if (session == null || !SessionManager.isSessionValid(db, session.id())) {
            // create new session
            session = SessionManager.createSession(db, username);
        }
        return AuthResult.success(user, session);
    }

    // ─────────────────────────────────────────────
    // 3) CONSTANT-TIME COMPARISON + UTILITIES
    // ─────────────────────────────────────────────
    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        byte[] x = a.getBytes(StandardCharsets.UTF_8);
        byte[] y = b.getBytes(StandardCharsets.UTF_8);
        int len = Math.max(x.length, y.length);
        int diff = 0;
        for (int i = 0; i < len; i++) {
            int xb = (i < x.length) ? x[i] : 0;
            int yb = (i < y.length) ? y[i] : 0;
            diff |= xb ^ yb;
        }
        return diff == 0 && x.length == y.length;
    }

    private static void fakeDelay(String plain) {
        // simple constant delay (simulates hashing cost)
        try {
            Thread.sleep(50);
        } catch (InterruptedException ignored) {}
    }

    // ─────────────────────────────────────────────
    // 4) RESULT RECORD
    // ─────────────────────────────────────────────
    public record AuthResult(boolean ok, String error, UserData user, Session session) {
        public static AuthResult success(UserData u, Session s) { return new AuthResult(true, null, u, s); }
        public static AuthResult failure(String err) { return new AuthResult(false, err, null, null); }
    }
}