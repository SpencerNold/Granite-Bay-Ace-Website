package com.granitebayace.site.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.objects.Hashing;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.http.HttpResponse;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;

import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service.Controller(path = "/api")
public class LoginController extends Implementation {

    @Route(method = Http.Method.POST, path = "/login", input = true, encoding = Route.Encoding.JSON)
    public HttpResponse login(JsonElement element) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Date", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME)); // HTTP date pattern
        if (!element.isJsonObject()) {
            JsonObject object = Response.malformedInput();
            return new HttpResponse(400, headers, object.toString().getBytes(StandardCharsets.UTF_8));
        }

        JsonObject input = element.getAsJsonObject();

        //Missing username or password
        if (!input.has("username") || !input.has("password")) {
            JsonObject object = Response.malformedInput();
            return new HttpResponse(400, headers, object.toString().getBytes(StandardCharsets.UTF_8));
        }

        JsonElement uElement = input.get("username");
        JsonElement pElement = input.get("password");

        //Not primitive values
        if (!uElement.isJsonPrimitive() || !pElement.isJsonPrimitive()) {
            JsonObject object = Response.malformedInput();
            return new HttpResponse(400, headers, object.toString().getBytes(StandardCharsets.UTF_8));
        }
        String username = uElement.getAsString();
        String password = pElement.getAsString();
        Hashing.AuthResult result;
        try {
            result = Hashing.login(getDatabase(), username, password, 0L);
        } catch (Exception e) {
            return new HttpResponse(401, headers, Response.invalidCredentials().toString().getBytes(StandardCharsets.UTF_8));
        }

        //Invalid credentials
        if (!result.ok()) {
            JsonObject object = Response.invalidCredentials();
            return new HttpResponse(401, headers, object.toString().getBytes(StandardCharsets.UTF_8));
        }
        String sessionId = result.session().id();
        JsonObject object = Response.success(sessionId, username);
        String cookie = String.format("session=%s;", result.session().id());
        headers.put("Set-Cookie", cookie + " HttpOnly; Secure; SameSite=Strict; Path=/; Max-Age=604800"); // 7 Days
        return new HttpResponse(200, headers, object.toString().getBytes(StandardCharsets.UTF_8));
    }

    //For reuseability
    private static class Response {

        private static JsonObject malformedInput() {
            JsonObject obj = new JsonObject();
            obj.addProperty("key", "error");
            obj.addProperty("message", "malformed input");
            return obj;
        }

        //Error message will not reveal why credentials are invalid
        private static JsonObject invalidCredentials() {
            JsonObject obj = new JsonObject();
            obj.addProperty("key", "error");
            obj.addProperty("ok", false);
            obj.addProperty("error", "invalid credentials");
            obj.addProperty("message", "Invalid username or password");
            return obj;
        }

        private static JsonObject success(String sessionId, String username) {
            JsonObject obj = new JsonObject();
            obj.addProperty("key", sessionId);
            obj.addProperty("message", "ok");
            obj.addProperty("username", username);
            return obj;
        }
    }

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }
}
