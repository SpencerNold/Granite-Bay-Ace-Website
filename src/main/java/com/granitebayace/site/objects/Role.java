package com.granitebayace.site.objects;

public record Role(int id, String name, int inheritance) {
    @Override
    public String toString() {
        return String.format("{id: %d, name: %s, inheritance: %d}", id, name, inheritance);
    }
}
