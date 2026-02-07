package com.granitebayace.site;

import com.granitebayace.site.services.LoginController;
import com.granitebayace.site.services.RootController;
import com.granitebayace.site.services.ScriptController;
import com.granitebayace.site.services.StyleController;
import com.granitebayace.site.services.MediaController;
import me.spencernold.kwaf.Protocol;
import me.spencernold.kwaf.WebServer;

import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        WebServer server = new WebServer.Builder(
                Protocol.HTTP,
                80,
                new Class[]{DatabaseLayer.class, RootController.class, StyleController.class, ScriptController.class, LoginController.class, MediaController.class},
                Executors.newCachedThreadPool(),
                false).build();
        server.start();
    }
}
