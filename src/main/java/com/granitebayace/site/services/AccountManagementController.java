package com.granitebayace.site.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.objects.Hashing;
import com.granitebayace.site.objects.Role;
import com.granitebayace.site.objects.Session;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;

@Service.Controller(path = "/api/accounts")
public class AccountManagementController extends Implementation {

    // Create a new manager account (admin only)
    @Route(method = Http.Method.POST, path = "/add", input = true, encoding = Route.Encoding.JSON)
    public JsonObject addManager(JsonElement element) {
        JsonObject out = new JsonObject();

        JsonObject input = requireObject(element, out);
        if (input == null) return out;

        String sessionKey = requireString(input, "sessionKey", out);
        String username = requireString(input, "username", out);
        String password = requireString(input, "password", out);
        if (sessionKey == null || username == null || password == null) return out;

        DatabaseLayer db = getDatabase();

        // Admin-only enforcement
        UserData caller = db.queryUserDataBySession(sessionKey);
        if (caller == null) return forbidden(out);
        if (caller.role() == null || caller.role().id() != 0) return forbidden(out);

        // Prevent duplicates
        if (db.containsUserData(username)) {
            out.add("message", new JsonPrimitive("user already exists"));
            return out;
        }

        Role managerRole = db.queryRole(1);
        if (managerRole == null) {
            out.add("message", new JsonPrimitive("manager role missing"));
            return out;
        }

        // Match existing codebase hashing/salting
        String salt = Hashing.generateSalt();
        String passhash = Hashing.hashForStorage(salt, password);

        // New account starts with no session
        Session session = null;

        UserData newManager = new UserData(username, passhash, salt, session, managerRole);
        db.insertUserData(newManager);

        out.add("message", new JsonPrimitive("ok"));
        return out;
    }

     // Change an existing user's role (admin only)
    @Route(method = Http.Method.POST, path = "/role", input = true, encoding = Route.Encoding.JSON)
    public JsonObject updateRole(JsonElement element) {
        JsonObject out = new JsonObject();

        JsonObject input = requireObject(element, out);
        if (input == null) return out;

        String sessionKey = requireString(input, "sessionKey", out);
        String username = requireString(input, "username", out);
        if (sessionKey == null || username == null) return out;

        if (!input.has("roleId") || !input.get("roleId").isJsonPrimitive()) {
            out.add("message", new JsonPrimitive("malformed input"));
            return out;
        }

        int roleId = input.get("roleId").getAsInt();
        if (roleId != 0 && roleId != 1) {
            out.add("message", new JsonPrimitive("invalid role"));
            return out;
        }

        DatabaseLayer db = getDatabase();

        // Admin only enforcement
        UserData caller = db.queryUserDataBySession(sessionKey);
        if (caller == null) return forbidden(out);
        if (caller.role() == null || caller.role().id() != 0) return forbidden(out);

        if (!db.containsUserData(username)) {
            out.add("message", new JsonPrimitive("user not found"));
            return out;
        }

        db.setUserRole(username, roleId);
        out.add("message", new JsonPrimitive("ok"));
        return out;
    }

    // Delete an account only if it currently has manager role (admin only)
    @Route(method = Http.Method.POST, path = "/delete", input = true, encoding = Route.Encoding.JSON)
    public JsonObject deleteManager(JsonElement element) {
        JsonObject out = new JsonObject();

        JsonObject input = requireObject(element, out);
        if (input == null) return out;

        String sessionKey = requireString(input, "sessionKey", out);
        String username = requireString(input, "username", out);
        if (sessionKey == null || username == null) return out;

        DatabaseLayer db = getDatabase();

        UserData caller = db.queryUserDataBySession(sessionKey);
        if (caller == null) return forbidden(out);
        if (caller.role() == null || caller.role().id() != 0) return forbidden(out);

        UserData target = db.queryUserData(username);
        if (target == null) {
            out.add("message", new JsonPrimitive("user not found"));
            return out;
        }

        // Only delete manager accounts
        if (target.role() == null || target.role().id() != 1) {
            out.add("message", new JsonPrimitive("target is not a manager"));
            return out;
        }

        db.deleteUser(username);
        out.add("message", new JsonPrimitive("ok"));
        return out;
    }

    // Lists all users for the manage account page table
    @Route(method = Http.Method.POST, path = "/list", input = true, encoding = Route.Encoding.JSON)
    public JsonObject listAccounts(JsonElement element) {
        JsonObject out = new JsonObject();

        JsonObject input = requireObject(element, out);
        if (input == null) return out;

        String sessionKey = requireString(input, "sessionKey", out);
        if (sessionKey == null) return out;

        DatabaseLayer db = getDatabase();

        UserData caller = db.queryUserDataBySession(sessionKey);
        if (caller == null) return forbidden(out);

        com.google.gson.JsonArray userList = new com.google.gson.JsonArray();

        try (java.sql.Statement stmt = db.getConnection().createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT username, role_id FROM user_data")) {

            while (rs.next()) {
                JsonObject u = new JsonObject();
                u.addProperty("username", rs.getString("username"));
                u.addProperty("roleId", rs.getInt("role_id"));
                userList.add(u);
            }

        } catch (java.sql.SQLException e) {
            out.add("message", new JsonPrimitive("error"));
            return out;
        }

        out.add("users", userList);
        out.addProperty("callerRole", caller.role().id());
        out.add("message", new JsonPrimitive("ok"));
        return out;
    }

    private JsonObject forbidden(JsonObject out) {
        out.add("message", new JsonPrimitive("forbidden"));
        return out;
    }

    private JsonObject requireObject(JsonElement element, JsonObject out) {
        if (element == null || !element.isJsonObject()) {
            out.add("message", new JsonPrimitive("malformed input"));
            return null;
        }
        return element.getAsJsonObject();
    }

    private String requireString(JsonObject input, String key, JsonObject out) {
        if (!input.has(key) || !input.get(key).isJsonPrimitive()) {
            out.add("message", new JsonPrimitive("malformed input"));
            return null;
        }
        return input.get(key).getAsString();
    }

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }
}