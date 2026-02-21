package com.granitebayace.site.commands.impl;

import com.granitebayace.site.DatabaseLayer;
import com.granitebayace.site.SessionManager;
import com.granitebayace.site.commands.Command;
import com.granitebayace.site.commands.Executor;
import com.granitebayace.site.objects.Hashing;
import com.granitebayace.site.objects.Role;
import com.granitebayace.site.objects.Session;
import com.granitebayace.site.objects.UserData;
import me.spencernold.kwaf.WebServer;
import me.spencernold.kwaf.logger.Logger;

import java.time.LocalDateTime;

@Command(label = "def-credential")
public class CredentialCommand implements Executor {

    private final WebServer server;

    public CredentialCommand(WebServer server) {
        this.server = server;
    }

    @Override
    public Result execute(String[] args) {
        DatabaseLayer layer = getDatabase();
        if (layer == null) {
            return Result.MALFORMED;
        }
        String salt = Hashing.generateSalt();
        String passhash = Hashing.hashForStorage(salt, "WiderSacramentoAces37!");
        Role role = layer.queryRole(0);
        UserData user = new UserData("admin", passhash, salt, SessionManager.createSession(layer, "admin"), role);
        layer.insertUserData(user);
        Hashing.AuthResult result = Hashing.login(layer, "admin", "WiderSacramentoAces37!", -1);
        if (!result.ok()) {
            return Result.MALFORMED;
        }
        Logger.Companion.getSystemLogger().info("Inserted default credentials!");
        return Result.SUCCESS;
    }

    private DatabaseLayer getDatabase() {
        return server.service(DatabaseLayer.class);
    }
}