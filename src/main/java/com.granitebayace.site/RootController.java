package com.granitebayace.site;

import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;

@Service.Controller
public class RootController {

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

    @Route.File(path = "/login", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream login() {
        return Resource.Companion.get("pages/login.html");
    }

    @Route.File(path = "/footer.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream footerHtml() {
        return Resource.Companion.get("footer/footer.html");
    }

    @Route.File(path = "/footer.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream footerCss() {
        return Resource.Companion.get("footer/footer.css");
    }

    @Route.File(path = "/navbar.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream navbarHtml() {
        return Resource.Companion.get("navbar/navbar.html");
    }

    @Route.File(path = "/navbar.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream navbarCss() {
        return Resource.Companion.get("navbar/navbar.css");
    }

    @Route.File(path = "/import.js", contentType = Route.ContentType.JAVASCRIPT, cacheControl = "no-cache")
    public InputStream importJs() {
        return Resource.Companion.get("scripts/import.js");
    }
}
