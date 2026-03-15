package com.granitebayace.site.services;

import com.google.gson.JsonObject;
import com.granitebayace.site.DatabaseLayer;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.http.HttpRequest;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;
import me.spencernold.kwaf.util.InputStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service.Controller
public class SecurePageController extends Implementation implements SecureService {

    @Route(method = Http.Method.GET, path = "/admin", encoding = Route.Encoding.RAW, contentType = Route.ContentType.HTML)
    public String admin(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        if (!isAdmin(getDatabase(), headers))
            return redirect();
        return buildPage("pages/admin.html");
    }

    @Route(method = Http.Method.GET, path = "/AdminNavbar.html", encoding = Route.Encoding.RAW, contentType = Route.ContentType.HTML)
    public String adminNavBar(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        if (!isPrivileged(getDatabase(), headers))
            return redirect();
        return buildPage("Admin/AdminNavbar.html");
    }

    @Route(method = Http.Method.GET, path = "/roles", encoding = Route.Encoding.RAW, contentType = Route.ContentType.HTML)
    public String roles(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        if (!isAdmin(getDatabase(), headers))
            return redirect();
        return buildPage("pages/roles.html");
    }

    @Route(method = Http.Method.GET, path = "/manage-account", encoding = Route.Encoding.RAW, contentType = Route.ContentType.HTML)
    public String manageAccount(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        if (!isAdmin(getDatabase(), headers))
            return redirect();
        return buildPage("pages/manage-account.html");
    }

    @Route(method = Http.Method.GET, path = "/get-role-value", encoding = Route.Encoding.JSON)
    public JsonObject role(HttpRequest request) {
        JsonObject object = new JsonObject();
        int level = getRolePermissionLevel(getDatabase(), request.getHeaders());
        object.addProperty("level", level);
        return object;
    }

    private String redirect() {
        return buildPage("pages/redirect.html");
    }

    private String buildPage(String path) {
        InputStream input = Resource.Companion.get(path);
        if (input == null)
            return redirect();
        try {
            return new String(InputStreams.Companion.readAllBytes(input));
        } catch (IOException ignored) {
        }
        return redirect();
    }

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }
}
