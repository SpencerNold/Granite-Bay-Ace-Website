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
        JsonObject object = new JsonObject();
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Date", ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.RFC_1123_DATE_TIME)); // HTTP date pattern
        if (!element.isJsonObject()) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive("malformed input"));
            return new HttpResponse(400, headers, object.toString().getBytes(StandardCharsets.UTF_8));
        }
        JsonObject input = element.getAsJsonObject();
        if (!input.has("username") || !input.has("password")) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive("malformed input (missing username and password)"));
            return new HttpResponse(400, headers, object.toString().getBytes(StandardCharsets.UTF_8));
        }
        JsonElement uElement = input.get("username");
        JsonElement pElement = input.get("password");
        if (!uElement.isJsonPrimitive() || !pElement.isJsonPrimitive()) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive("malformed input (type mismatch for username and password)"));
            return new HttpResponse(400, headers, object.toString().getBytes(StandardCharsets.UTF_8));
        }
        String username = uElement.getAsString();
        String password = pElement.getAsString();
        Hashing.AuthResult result;
        try {
            result = Hashing.login(getDatabase(), username, password, 0L);
        } catch (Exception e) {
            return new HttpResponse(401, headers, object.toString().getBytes(StandardCharsets.UTF_8));
        }
        if (!result.ok()) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive(result.error()));
            return new HttpResponse(401, headers, object.toString().getBytes(StandardCharsets.UTF_8));
        }
        object.add("key", new JsonPrimitive(result.session().id()));
        object.add("message", new JsonPrimitive("ok"));
        String cookie = String.format("session=%s;", result.session().id());
        headers.put("Set-Cookie", cookie + " HttpOnly; Secure; SameSite=Strict; Path=/; Max-Age=604800"); // 7 Days
        return new HttpResponse(200, headers, object.toString().getBytes(StandardCharsets.UTF_8));
    }

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }
}
