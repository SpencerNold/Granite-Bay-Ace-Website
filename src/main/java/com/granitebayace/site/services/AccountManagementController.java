package com.granitebayace.site.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;

@Service.Controller(path = "/api/accounts")
public class AccountManagementController extends Implementation {

    // Give an existing account manager access (admin can only use this)
    @Route(method = Http.Method.POST, path = "/add", input = true, encoding = Route.Encoding.JSON)
    public JsonObject addManager(JsonElement element) {
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

        if (!db.containsUserData(username)) {
            out.add("message", new JsonPrimitive("user not found"));
            return out;
        }

        db.setUserRole(username, 1);
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