package com.granitebayace.site;

import com.granitebayace.site.services.*;
import com.granitebayace.site.commands.impl.CredentialCommand;
import com.granitebayace.site.commands.CommandRegistry;
import com.granitebayace.site.commands.Executor;
import com.granitebayace.site.services.*;
import me.spencernold.kwaf.Protocol;
import me.spencernold.kwaf.WebServer;
import me.spencernold.kwaf.logger.Logger;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        // Build WebServer
        WebServer server = new WebServer.Builder(
                Protocol.HTTP,
                80,
                new Class[]{DatabaseLayer.class, RootController.class, StyleController.class, ScriptController.class, LoginController.class, AccountManagementController.class, SecurePageController.class, MediaController.class},
                Executors.newCachedThreadPool(),
                false).build();
        // Register Commands
        CommandRegistry commands = CommandRegistry.get();
        commands.register(new CredentialCommand(server));
        // Handle stdin
        try (ExecutorService service = Executors.newSingleThreadExecutor()) {
            service.execute(() -> {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()) {
                    String in = scanner.nextLine();
                    Executor.Result result = commands.execute(in.split(" "));
                    Logger logger = Logger.Companion.getSystemLogger();
                    switch (result) {
                        case UNKNOWN -> {
                            logger.error("Unknown command!");
                        }
                        case MALFORMED -> {
                            logger.error("Command is missing proper arguments!");
                            break;
                        }
                        case UNAUTHORIZED -> {
                            logger.error("You are not authorized to use this command!");
                            break;
                        }
                        case SUCCESS -> {
                        }
                    }
                }
            });
        }
        server.start();
    }
}
