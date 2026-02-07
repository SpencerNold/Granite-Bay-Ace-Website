package com.granitebayace.site.services;

import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;

@Service.Controller
public class MediaController {

    @Route.File(path = "/images/page01.png", contentType = Route.ContentType.PNG, cacheControl = "public, no-cache")
    public InputStream img01() {
        return Resource.Companion.get("images/page01.png");
    }

    @Route.File(path = "/images/page02.png", contentType = Route.ContentType.PNG, cacheControl = "public, no-cache")
    public InputStream img02() {
        return Resource.Companion.get("images/page02.png");
    }

    @Route.File(path = "/images/page03.png", contentType = Route.ContentType.PNG, cacheControl = "public, no-cache")
    public InputStream img03() {
        return Resource.Companion.get("images/page03.png");
    }

    @Route.File(path = "/images/page04.png", contentType = Route.ContentType.PNG, cacheControl = "public, no-cache")
    public InputStream img04() {
        return Resource.Companion.get("images/page04.png");
    }

    @Route.File(path = "/images/page05.png", contentType = Route.ContentType.PNG, cacheControl = "public, no-cache")
    public InputStream img05() {
        return Resource.Companion.get("images/page05.png");
    }

    @Route.File(path = "/images/page06.png", contentType = Route.ContentType.PNG, cacheControl = "public, no-cache")
    public InputStream img06() {
        return Resource.Companion.get("images/page06.png");
    }

    @Route.File(path = "/images/page07.png", contentType = Route.ContentType.PNG, cacheControl = "public, no-cache")
    public InputStream img07() {
        return Resource.Companion.get("images/page07.png");
    }

    @Route.File(path = "/images/page08.png", contentType = Route.ContentType.PNG, cacheControl = "public, no-cache")
    public InputStream img08() {
        return Resource.Companion.get("images/page08.png");
    }

}
