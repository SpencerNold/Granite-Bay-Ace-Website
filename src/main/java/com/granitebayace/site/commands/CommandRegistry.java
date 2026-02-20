package com.granitebayace.site.commands;

import me.spencernold.kwaf.logger.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CommandRegistry {

    private static CommandRegistry instance;

    private final Map<String, Executor> lookupTable = new HashMap<>();

    private CommandRegistry() {}

    public void register(Executor executor) {
        Class<?> clazz = executor.getClass();
        if (!clazz.isAnnotationPresent(Command.class)) {
            Logger.Companion.getSystemLogger().warn(clazz + " is missing @Command annotation, failing non-fatally");
            return;
        }
        Command command = clazz.getAnnotation(Command.class);
        lookupTable.put(command.label(), executor);
    }

    public Executor.Result execute(String[] command) {
        String label = command[0];
        String[] args = Arrays.copyOfRange(command, 1, command.length);
        if (!lookupTable.containsKey(label))
            return Executor.Result.UNKNOWN;
        return lookupTable.get(label).execute(args);
    }

    public static CommandRegistry get() {
        return instance == null ? instance = new CommandRegistry() : instance;
    }
}
