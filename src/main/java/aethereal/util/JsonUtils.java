package aethereal.util;


import com.google.gson.JsonObject;

import java.util.Optional;

public class JsonUtils {
    private JsonUtils() {
    }

    public static Optional<String> a(JsonObject json, String field) {
        if (json == null || !json.has(field) || json.get(field).isJsonNull()) {
            return Optional.empty();
        }
        return Optional.of(json.get(field).getAsString());
    }

    public static String a(JsonObject json, String field, String defaultValue) {
        return a(json, field).orElse(defaultValue);
    }

    public static Optional<JsonObject> b(JsonObject json, String field) {
        if (json == null || !json.has(field) || !json.get(field).isJsonObject()) {
            return Optional.empty();
        }
        return Optional.of(json.getAsJsonObject(field));
    }

    public static boolean a(JsonObject json, String field, boolean defaultValue) {
        if (json == null || !json.has(field) || json.get(field).isJsonNull()) {
            return defaultValue;
        }
        return json.get(field).getAsBoolean();
    }

    public static int a(JsonObject json, String field, int defaultValue) {
        if (json == null || !json.has(field) || json.get(field).isJsonNull()) {
            return defaultValue;
        }
        return json.get(field).getAsInt();
    }
}
