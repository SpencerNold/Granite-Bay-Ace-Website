package com.granitebayace.site.commands;

@FunctionalInterface
public interface Executor {
    Result execute(String[] args);

    enum Result {
        UNKNOWN, MALFORMED, UNAUTHORIZED, SUCCESS
    }
}
