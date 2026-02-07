package com.granitebayace.site.services;

import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;

@Service.Controller
public class ScriptController {

    @Route.File(path = "/import.js", contentType = Route.ContentType.JAVASCRIPT, cacheControl = "no-cache")
    public InputStream importJs() {
        return Resource.Companion.get("scripts/import.js");
    }

    @Route.File(path = "/admin.js", contentType = Route.ContentType.JAVASCRIPT, cacheControl = "no-cache")
    public InputStream adminJs() {
        return Resource.Companion.get("scripts/admin.js");
    }

    @Route.File(path = "/login.js", contentType = Route.ContentType.JAVASCRIPT, cacheControl = "no-cache")
    public InputStream loginJs() {
        return Resource.Companion.get("scripts/login.js");
    }
}
