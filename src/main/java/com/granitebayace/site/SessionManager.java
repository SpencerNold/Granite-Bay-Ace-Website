package com.granitebayace.site;

import com.granitebayace.site.objects.Session;
import com.granitebayace.site.objects.UserData;

import java.time.LocalDateTime;
import java.security.SecureRandom;
import java.util.Base64;

public class SessionManager {
    private static SessionManager instance;
    private static final SecureRandom rand = new SecureRandom();


    public static Session createSession(DatabaseLayer db, String username) {
        UserData user = db.queryUserData(username);
        if (user == null) { return null; }
        if (user.session() != null) {
            return user.session();
        }
        else {
            String id = generateUniqueKey(db);

            LocalDateTime expiration = LocalDateTime.now().plusYears(1);

            Session newSession = new Session(id, expiration);
            db.insertSession(username, newSession);

            return newSession;
        }
    }

    private static String generateUniqueKey(DatabaseLayer db) {
        String sessionKey;
        do {
            byte[] bytes = new byte[32];
            rand.nextBytes(bytes);
            sessionKey = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        } while (db.containsSession(sessionKey));
        return sessionKey;
    }

    public static boolean isSessionValid(DatabaseLayer db, String id) {
        Session s = db.querySession(id);
        if (s == null) { return false; }
        if (s.expiration().isBefore(LocalDateTime.now())) {
            return false;
        }
        else { return true; }
    }
}
