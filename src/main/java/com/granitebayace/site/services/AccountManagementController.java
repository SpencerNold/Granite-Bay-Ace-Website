package com.granitebayace.site.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonArray;
import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;

@Service.Controller(path = "/api/accounts")
public class AccountManagementController extends Implementation {

    // Give an existing account manager access (admin can only use this), can also update user role
    @Route(method = Http.Method.POST, path = "/add", input = true, encoding = Route.Encoding.JSON)
    public JsonObject addManager(JsonElement element) {
        JsonObject out = new JsonObject();

        JsonObject input = element.getAsJsonObject();
        String sessionKey = requireString(input, "sessionKey", out);
        String username = requireString(input, "username", out);
        int targetRoleId = input.has("roleId") ? input.get("roleId").getAsInt() : 1;

        DatabaseLayer db = getDatabase();

        UserData caller = db.queryUserDataBySession(sessionKey);
        if (caller == null) return forbidden(out);
        if (caller.role() == null || caller.role().id() != 0) return forbidden(out); // Check if admin only

        if (!db.containsUserData(username)) {
            out.add("message", new JsonPrimitive("user not found"));
            return out;
        }

        db.setUserRole(username, targetRoleId);
        out.add("message", new JsonPrimitive("ok"));
        return out;
    }

    // Delete/remove an account from having manager privileges (admin only can use this).
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
        if (caller.role() == null || caller.role().id() != 0) return forbidden(out); // Check if admin only

        UserData target = db.queryUserData(username);
        if (target == null) {
            out.add("message", new JsonPrimitive("user not found"));
            return out;
        }
        // Only delete accounts that currently have manager privileges
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
        String sessionKey = requireString(element.getAsJsonObject(), "sessionKey", out);
        DatabaseLayer db = getDatabase();

        UserData caller = db.queryUserDataBySession(sessionKey);
        if (caller == null) return forbidden(out);

        com.google.gson.JsonArray userList = new com.google.gson.JsonArray();
        // Manual query to get all users for the management table
        try (java.sql.Statement stmt = db.getConnection().createStatement();
             java.sql.ResultSet rs = stmt.executeQuery("SELECT username, role_id FROM user_data")) {
            while (rs.next()) {
                JsonObject u = new JsonObject();
                u.addProperty("username", rs.getString("username"));
                u.addProperty("roleId", rs.getInt("role_id"));
                userList.add(u);
            }
        } catch (java.sql.SQLException e) {
            out.addProperty("message", "error");
        }

        out.add("users", userList);
        out.addProperty("callerRole", caller.role().id());
        out.addProperty("message", "ok");
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