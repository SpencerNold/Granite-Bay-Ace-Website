package com.granitebayace.site.services;

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
public class RootController extends Implementation {

    @Route.File(path = "/", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream index() {
        return Resource.Companion.get("pages/index.html");
    }

    @Route.File(path = "/about", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream about() {
        return Resource.Companion.get("pages/about.html");
    }

    @Route.File(path = "/locations.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream locationsHtml() {
        return Resource.Companion.get("pages/locations.html");
    }

    @Route.File(path = "/login", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream login() {
        return Resource.Companion.get("pages/login.html");
    }

    @Route.File(path = "/footer.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream footerHtml() {
        return Resource.Companion.get("footer/footer.html");
    }

    @Route.File(path = "/navbar.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream navbarHtml() {
        return Resource.Companion.get("navbar/navbar.html");
    }

    @Route.File(path = "/statements.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream statementsHtml() {
        return Resource.Companion.get("pages/statements.html");
    }

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }
}