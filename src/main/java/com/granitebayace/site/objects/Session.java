package com.granitebayace.site.objects;

import java.time.LocalDateTime;

public record Session(String id, LocalDateTime expiration) {
    @Override
    public String toString() {
        return String.format("{id: %s, expiration: %s}", "#".repeat(id.length()), expiration.toString());
    }
}
