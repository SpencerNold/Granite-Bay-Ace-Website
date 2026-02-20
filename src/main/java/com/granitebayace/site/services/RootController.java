package com.granitebayace.site.services;

import com.granitebayace.site.DatabaseLayer;
import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;

@Service.Controller
public class RootController extends Implementation {

    @Route.File(path = "/", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream index() {
        return Resource.Companion.get("pages/index.html");
    }

    @Route.File(path = "/about", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream about() {
        return Resource.Companion.get("pages/about.html");
    }

    @Route.File(path = "/locations", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream locations() {
        return Resource.Companion.get("pages/locations.html");
    }

    @Route.File(path = "/locations.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream locationsHtml() {
        return Resource.Companion.get("pages/locations.html");
    }

    @Route.File(path = "/login", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream login() {
        return Resource.Companion.get("pages/login.html");
    }

    @Route.File(path = "/admin", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream admin() {
        return Resource.Companion.get("pages/admin.html");
    }

    @Route.File(path = "/footer.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream footerHtml() {
        return Resource.Companion.get("footer/footer.html");
    }

    @Route.File(path = "/navbar.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream navbarHtml() {
        return Resource.Companion.get("navbar/navbar.html");
    }

    @Route.File(path = "/accessibility.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream accessibilityHtml() {
        return Resource.Companion.get("pages/accessibility.html");
    }

    @Route.File(path = "/AdminNavbar.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream AdminNavbar() {
        return Resource.Companion.get("Admin/AdminNavbar.html");
    }

    @Route.File(path = "/roles", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream roles() {
        return Resource.Companion.get("pages/roles.html");
    }

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }
}
