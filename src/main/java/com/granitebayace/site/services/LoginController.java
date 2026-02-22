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
        JsonObject object = new JsonObject();

        //Not a json object > error
        if (!element.isJsonObject()) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive("malformed input"));
            return object;
        }

        //No username or password > error
        JsonObject input = element.getAsJsonObject();
        if (!input.has("username") || !input.has("password")) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive("malformed input"));
            return object;
        }
        JsonElement uElement = input.get("username");
        JsonElement pElement = input.get("password");

        //Make sure username and password are primitive values else error
        if (!uElement.isJsonPrimitive() || !pElement.isJsonPrimitive()) {
            object.add("key", new JsonPrimitive("error"));
            object.add("message", new JsonPrimitive("malformed input"));
            return object;
        }
        String username = uElement.getAsString();
        String password = pElement.getAsString();
        Hashing.AuthResult result = Hashing.login(getDatabase(), username, password, 0L);

        //Hashes the credentials and compares to database, if there isn't a matching pair then error
        if (!result.ok()) {
            // Don’t leak whether username exists / etc.
            object.add("key", new JsonPrimitive("error"));
            object.add("ok", new JsonPrimitive(false));
            object.add("error", new JsonPrimitive("invalid credentials"));
            object.add("message", new JsonPrimitive("Invalid username or passsword"));
            return object;
        }

        //Return the object
        //Lets user info be stored in local browser
        object.add("key", new JsonPrimitive(result.session().id()));
        object.add("message", new JsonPrimitive("ok"));
        object.add("username", new JsonPrimitive(username));
        return object;
    }

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }

}
