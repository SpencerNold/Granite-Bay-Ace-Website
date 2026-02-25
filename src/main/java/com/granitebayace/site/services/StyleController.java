package com.granitebayace.site.services;

import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;

@Service.Controller
public class StyleController {

    @Route.File(path = "/footer.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream footerCss() {
        return Resource.Companion.get("footer/footer.css");
    }

    @Route.File(path = "/navbar.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream navbarCss() {
        return Resource.Companion.get("navbar/navbar.css");
    }

    @Route.File(path = "/advertisement.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream advertisementCss() {
        return Resource.Companion.get("pages/advertisement.css");
    }

    @Route.File(path = "/about.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream aboutCss() {
        return Resource.Companion.get("pages/about.css");
    }

    @Route.File(path = "/locations.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream locationsCss() {
        return Resource.Companion.get("pages/locations.css");
    }

    @Route.File(path = "/statements.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream statementsCss() {
        return Resource.Companion.get("pages/statements.css");
    }

    @Route.File(path = "/login.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream loginCss() {
        return Resource.Companion.get("pages/login.css");
    }

    @Route.File(path = "/AdminNavbar.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream AdminNavbarCss() {
        return Resource.Companion.get("Admin/AdminNavbar.css");
    }

    @Route.File(path = "/admin.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream AdminCss() {
        return Resource.Companion.get("pages/admin.css");
    }

    @Route.File(path = "/roles.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream rolesCss() {
        return Resource.Companion.get("pages/roles.css");
    }

    @Route.File(path = "/manage-account.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream manageAccountCss() {
        return Resource.Companion.get("pages/manage-account.css");
    }
}
