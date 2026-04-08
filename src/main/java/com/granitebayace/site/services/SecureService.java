package com.granitebayace.site.services;

import com.google.gson.*;
import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.SessionManager;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.http.HttpRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface SecureService {

    Gson GSON = new GsonBuilder().create();

    default JsonObject getFromRequest(HttpRequest request, JsonObject out) {
        try (InputStreamReader reader = new InputStreamReader(new ByteArrayInputStream(request.getBody()))) {
            JsonElement element = GSON.fromJson(reader, JsonElement.class);
            return requireObject(element, out);
        } catch (IOException e) {
            out.add("message", new JsonPrimitive("malformed input"));
            return null;
        }
    }

    default JsonObject requireObject(JsonElement element, JsonObject out) {
        if (element == null || !element.isJsonObject()) {
            out.add("message", new JsonPrimitive("malformed input"));
            return null;
        }
        return element.getAsJsonObject();
    }

    default String requireString(JsonObject input, String key, JsonObject out) {
        if (!input.has(key) || !input.get(key).isJsonPrimitive()) {
            out.add("message", new JsonPrimitive("malformed input"));
            return null;
        }
        return input.get(key).getAsString();
    }

    default boolean isAdmin(DatabaseLayer database, Map<String, String> headers) {
        return getRolePermissionLevel(database, headers) == 0;
    }

    default boolean isPrivileged(DatabaseLayer database, Map<String, String> headers) {
        return getRolePermissionLevel(database, headers) <= 1;
    }

    default int getRolePermissionLevel(DatabaseLayer database, Map<String, String> headers) {
        String session = getExtractedSessionToken(headers);
        if (session == null)
            return Integer.MAX_VALUE;
        UserData data = database.queryUserDataBySession(session);
        if (data == null)
            return Integer.MAX_VALUE;
        if (data.session() == null || !SessionManager.isSessionValid(database, data.session().id()))
            return Integer.MAX_VALUE;
        return data.role().id();
    }

    default String getExtractedSessionToken(Map<String, String> headers) {
        if (!headers.containsKey("Cookie"))
            return null;
        String cookiesString = headers.get("Cookie");
        Pattern pattern = Pattern.compile("([^;\\s]+?)=([^;\\s]+?)(?=;|$)"); // matches key=value;
        Matcher matcher = pattern.matcher(cookiesString);
        Map<String, String> cookies = new HashMap<>();
        while (matcher.find())
            cookies.put(matcher.group(1), matcher.group(2));
        if (!cookies.containsKey("session"))
            return null;
        return cookies.get("session");
    }
}
