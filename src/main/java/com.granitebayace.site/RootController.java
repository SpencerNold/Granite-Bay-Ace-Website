package com.granitebayace.site;

import me.spencernold.kwaf.Resource;
import me.spencernold.kwaf.Route;
import me.spencernold.kwaf.services.Implementation;
import me.spencernold.kwaf.services.Service;

import java.io.InputStream;

@Service.Controller
public class RootController extends Implementation {

    //START HTML SECTION
    @Route.File(path = "/", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream index() { return Resource.Companion.get("pages/index.html"); }

    @Route.File(path = "/about", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream about() { return Resource.Companion.get("pages/about.html"); }

    @Route.File(path = "/locations", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream locations() { return Resource.Companion.get("pages/locations.html"); }

    @Route.File(path = "/locations.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream locationsHtml() { return Resource.Companion.get("pages/locations.html"); }

    @Route.File(path = "/login", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream login() { return Resource.Companion.get("pages/login.html"); }
    
    @Route.File(path = "/admin", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream admin() { return Resource.Companion.get("pages/admin.html"); }

    @Route.File(path = "/footer.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream footerHtml() { return Resource.Companion.get("footer/footer.html"); }

    @Route.File(path = "/navbar.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream navbarHtml() { return Resource.Companion.get("navbar/navbar.html"); }

    @Route.File(path = "/advertisement.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream advertisementHtml() { return Resource.Companion.get("pages/advertisement.html"); }
    
    @Route.File(path = "/accessibility.html", contentType = Route.ContentType.HTML, cacheControl = "no-store")
    public InputStream accessibilityHtml() { return Resource.Companion.get("pages/accessibility.html"); }
    //END HTML SECTION


    //START IMAGE SECTION
    @Route.File(path = "/images/page01.webp", contentType = Route.ContentType.WEBP,
            cacheControl = "public, max-age=31536000")
    public InputStream img01() { return Resource.Companion.get("images/page01.webp"); }

    @Route.File(path = "/images/page02.webp", contentType = Route.ContentType.WEBP,
            cacheControl = "public, max-age=31536000")
    public InputStream img02() { return Resource.Companion.get("images/page02.webp"); }

    @Route.File(path = "/images/page03.webp", contentType = Route.ContentType.WEBP,
            cacheControl = "public, max-age=31536000")
    public InputStream img03() { return Resource.Companion.get("images/page03.webp"); }

    @Route.File(path = "/images/page04.webp", contentType = Route.ContentType.WEBP,
            cacheControl = "public, max-age=31536000")
    public InputStream img04() { return Resource.Companion.get("images/page04.webp"); }

    @Route.File(path = "/images/page05.webp", contentType = Route.ContentType.WEBP,
            cacheControl = "public, max-age=31536000")
    public InputStream img05() { return Resource.Companion.get("images/page05.webp"); }

    @Route.File(path = "/images/page06.webp", contentType = Route.ContentType.WEBP,
            cacheControl = "public, max-age=31536000")
    public InputStream img06() { return Resource.Companion.get("images/page06.webp"); }

    @Route.File(path = "/images/page07.webp", contentType = Route.ContentType.WEBP,
            cacheControl = "public, max-age=31536000")
    public InputStream img07() { return Resource.Companion.get("images/page07.webp"); }

    @Route.File(path = "/images/page08.webp", contentType = Route.ContentType.WEBP,
            cacheControl = "public, max-age=31536000")
    public InputStream img08() { return Resource.Companion.get("images/page08.webp"); }
    //END IMAGE SECTION

    //START CSS SECTION
    @Route.File(path = "/footer.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream footerCss() { return Resource.Companion.get("footer/footer.css"); }

    @Route.File(path = "/navbar.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream navbarCss() { return Resource.Companion.get("navbar/navbar.css"); }

    @Route.File(path = "/advertisement.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream advertisementCss() {return Resource.Companion.get("pages/advertisement.css");}
    
    @Route.File(path = "/about.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream aboutCss() { return Resource.Companion.get("pages/about.css"); }

    @Route.File(path = "/locations.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream locationsCss() { return Resource.Companion.get("pages/locations.css"); }

    @Route.File(path = "/accessibility.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream accessibilityCss() { return Resource.Companion.get("pages/accessibility.css"); }
    
    @Route.File(path = "/login.css", contentType = Route.ContentType.CSS, cacheControl = "no-cache")
    public InputStream loginCss() { return Resource.Companion.get("pages/login.css"); }
    //END CSS SECTION
    
    //START JS SECTION
    @Route.File(path = "/import.js", contentType = Route.ContentType.JAVASCRIPT, cacheControl = "no-cache")
    public InputStream importJs() { return Resource.Companion.get("scripts/import.js"); }
    //END JS SECTION

    private DatabaseLayer getDatabase() {
        return getService(DatabaseLayer.class);
    }
}
