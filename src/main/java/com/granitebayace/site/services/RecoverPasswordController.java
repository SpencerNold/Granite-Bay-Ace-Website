package com.granitebayace.site.services;

import com.google.gson.JsonObject;
import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.SessionManager;
import com.granitebayace.site.objects.Hashing;
import com.granitebayace.site.objects.Session;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.http.HttpRequest;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;

@Service.Controller(path = "/api/recovery")
public class RecoverPasswordController extends Implementation implements SecureService {

    @Route(method = Http.Method.POST, path = "/reset", input = true, encoding = Route.Encoding.JSON)
    public JsonObject resetPassword(HttpRequest request) {
        JsonObject out = new JsonObject();
        out.addProperty("status", "forbidden");
        JsonObject input = getFromRequest(request, out);
        if (input == null)
            return out;
        DatabaseLayer db = getDatabase();
        if (!isAdmin(db, request.getHeaders()))
            return out;
        if (!input.has("username") || !input.get("username").isJsonPrimitive() || !input.has("password") || !input.get("password").isJsonPrimitive())
            return out;
        String username = input.get("username").getAsString();
        String password = input.get("password").getAsString();
        UserData data = db.queryUserData(username);
        if (data == null)
            return out;

        String newSalt = Hashing.generateSalt();
        String passhash = Hashing.hashForStorage(newSalt, password);
        Session session = data.session();
        if (session == null || !SessionManager.isSessionValid(db, session.id()))
            session = SessionManager.createSession(db, username);
        data = new UserData(data.username(), passhash, newSalt, session, data.role());
        db.insertUserData(data);
        out.addProperty("status", "ok");
        return out;
    }

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }
}
