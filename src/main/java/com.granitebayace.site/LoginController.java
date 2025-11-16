package com.granitebayace.site;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.granitebayace.site.objects.Hashing;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;

@Service.Controller(path = "/api")
public class LoginController extends Implementation {

    @Route(method = Http.Method.POST, path = "/login", input = true, encoding = Route.Encoding.JSON)
    public JsonObject login(JsonElement element) {
        JsonObject object = new JsonObject();
        if (!element.isJsonObject()) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive("malformed input"));
            return object;
        }
        JsonObject input = element.getAsJsonObject();
        if (!input.has("username") || !input.has("password")) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive("malformed input"));
            return object;
        }
        JsonElement uElement = input.get("username");
        JsonElement pElement = input.get("password");
        if (!uElement.isJsonPrimitive() || !pElement.isJsonPrimitive()) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive("malformed input"));
            return object;
        }
        String username = uElement.getAsString();
        String password = pElement.getAsString();
        Hashing.AuthResult result = Hashing.login(getDatabase(), username, password, 0L);
        if (!result.ok()) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive(result.error()));
            return object;
        }
        object.add("key", new JsonPrimitive(result.session().id()));
        object.add("message", new JsonPrimitive("ok"));
        return object;
    }

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }
}
