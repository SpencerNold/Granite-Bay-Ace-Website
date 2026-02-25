package com.granitebayace.site.services;

import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.SessionManager;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.Http;
import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.http.HttpRequest;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;
import me.spencernold.kwaf.util.InputStreams;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service.Controller
public class SecurePageController extends Implementation {

    @Route(method = Http.Method.GET, path = "/admin", encoding = Route.Encoding.RAW, contentType = Route.ContentType.HTML)
    public String admin(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        if (!isAdmin(headers))
            return redirect();
        return buildPage("pages/admin.html");
    }

    @Route(method = Http.Method.GET, path = "/AdminNavbar.html", encoding = Route.Encoding.RAW, contentType = Route.ContentType.HTML)
    public String adminNavBar(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        if (!isPrivileged(headers))
            return redirect();
        return buildPage("Admin/AdminNavbar.html");
    }

    @Route(method = Http.Method.GET, path = "/roles", encoding = Route.Encoding.RAW, contentType = Route.ContentType.HTML)
    public String roles(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        if (!isAdmin(headers))
            return redirect();
        return buildPage("pages/roles.html");
    }

    @Route(method = Http.Method.GET, path = "/manage-account", encoding = Route.Encoding.RAW, contentType = Route.ContentType.HTML)
    public String manageAccount(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        if (!isAdmin(headers))
            return redirect();
        return buildPage("pages/manage-account.html");
    }

    private boolean isAdmin(Map<String, String> headers) {
        return getRolePermissionLevel(headers) == 0;
    }

    private boolean isPrivileged(Map<String, String> headers) {
        return getRolePermissionLevel(headers) <= 1;
    }

    private int getRolePermissionLevel(Map<String, String> headers) {
        if (!headers.containsKey("Cookie"))
            return Integer.MAX_VALUE;
        String cookiesString = headers.get("Cookie");
        Pattern pattern = Pattern.compile("([^;\\s]+?)=([^;\\s]+?)(?=;|$)"); // matches key=value;
        Matcher matcher = pattern.matcher(cookiesString);
        Map<String, String> cookies = new HashMap<>();
        while (matcher.find())
            cookies.put(matcher.group(1), matcher.group(2));
        if (!cookies.containsKey("session"))
            return Integer.MAX_VALUE;
        DatabaseLayer database = getDatabase();
        UserData data = database.queryUserDataBySession(cookies.get("session"));
        if (data == null)
            return Integer.MAX_VALUE;
        if (data.session() == null || !SessionManager.isSessionValid(database, data.session().id()))
            return Integer.MAX_VALUE;
        return data.role().id();
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
