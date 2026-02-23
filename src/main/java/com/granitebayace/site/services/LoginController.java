package com.granitebayace.site.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.objects.Hashing;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;

@Service.Controller(path = "/api")
public class LoginController extends Implementation {

    @Route(method = Http.Method.POST, path = "/login", input = true, encoding = Route.Encoding.JSON)
    public JsonObject login(JsonElement element) {

        //Not a JSON object
        if (!element.isJsonObject()) {
            return Response.malformedInput();
        }

        JsonObject input = element.getAsJsonObject();

        //Missing username or password
        if (!input.has("username") || !input.has("password")) {
            return Response.malformedInput();
        }

        JsonElement uElement = input.get("username");
        JsonElement pElement = input.get("password");

        //Not primitive values
        if (!uElement.isJsonPrimitive() || !pElement.isJsonPrimitive()) {
            return Response.malformedInput();
        }

        String username = uElement.getAsString();
        String password = pElement.getAsString();

        Hashing.AuthResult result =
                Hashing.login(getDatabase(), username, password, 0L);

        //Invalid credentials
        if (!result.ok()) {
            return Response.invalidCredentials();
        }

        return Response.success(result.session().id(), username);
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

