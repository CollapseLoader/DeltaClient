package aethereal.network;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class PacketSecurity {

    private Gson gson;

    public PacketSecurity() {
        initSecurity();
    }

    public static String convertElementToString(JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return null;
        }
        return element.getAsString();
    }

    public String extractString(String jsonPayload, String key) {
        if (jsonPayload == null || jsonPayload.isBlank()) {
            return null;
        }
        Object parsed = gson.fromJson(jsonPayload, JsonObject.class);
        if (!(parsed instanceof JsonObject jsonObject)) {
            throw new ClassCastException("Ожидался JsonObject");
        }
        if (jsonObject.has(key) && !jsonObject.get(key).isJsonNull()) {
            return jsonObject.get(key).getAsString();
        }
        return null;
    }

    public JsonElement extractElement(String jsonPayload, String key) {
        if (jsonPayload == null || jsonPayload.isBlank()) {
            return null;
        }
        Object parsed = gson.fromJson(jsonPayload, JsonObject.class);
        if (parsed == null) {
            return null;
        }
        if (!(parsed instanceof JsonObject jsonObject)) {
            throw new ClassCastException("Ожидался JsonObject");
        }
        if (!jsonObject.has(key)) {
            return null;
        }
        JsonElement element = jsonObject.get(key);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        return element;
    }

    public List<String> extractStringList(String jsonPayload, String key) {
        if (jsonPayload == null || jsonPayload.isBlank()) {
            return null;
        }
        Object parsed = gson.fromJson(jsonPayload, JsonObject.class);
        if (!(parsed instanceof JsonObject jsonObject)) {
            return null;
        }
        if (!jsonObject.has(key)) {
            return null;
        }
        JsonElement element = jsonObject.get(key);
        if (element == null || !element.isJsonArray()) {
            return null;
        }
        JsonArray jsonArray = jsonObject.getAsJsonArray(key);
        if (jsonArray == null) {
            return null;
        }
        return StreamSupport.stream(jsonArray.spliterator(), false)
                .map(e -> convertElementToString(e))
                .collect(Collectors.toList());
    }

    public String buildJson(Object... keyValues) {
        if (keyValues == null || (keyValues.length & 1) != 0) {
            throw new IllegalArgumentException("keyValues должно содержать чётное количество элементов");
        }
        JsonObject jsonObject = new JsonObject();
        for (int i = 0; i < keyValues.length; i += 2) {
            String key = String.valueOf(keyValues[i]);
            Object value = keyValues[i + 1];
            JsonElement jsonValue = convertToJsonElement(value);
            jsonObject.add(key, jsonValue);
        }
        return gson.toJson(jsonObject);
    }

    public String wrapPacket(String packetId, String payload) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", packetId);
        jsonObject.add("payload", gson.fromJson(payload, JsonElement.class));
        return gson.toJson(jsonObject);
    }

    public Optional<PacketData> unpackPacket(String message) {
        try {
            JsonObject jsonObject = gson.fromJson(message, JsonObject.class);
            if (!jsonObject.has("id") || !jsonObject.has("payload")
                    || jsonObject.get("id").isJsonNull()
                    || jsonObject.get("id").getAsString().isBlank()) {
                return Optional.empty();
            }
            String id = jsonObject.get("id").isJsonNull() ? null : jsonObject.get("id").getAsString();
            String payload = jsonObject.get("payload").getAsString();
            return Optional.of(new PacketData(id, payload));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public String encrypt(String plainText) {
        return null;
    }

    public String decrypt(String encoded) {
        return null;
    }

    private JsonElement convertToJsonElement(Object value) {
        if (value instanceof JsonNode) {
            return JsonParser.parseString(value.toString());
        }
        if (value instanceof Number) {
            return new JsonPrimitive((Number) value);
        }
        if (value instanceof Boolean) {
            return new JsonPrimitive((Boolean) value);
        }
        return gson.toJsonTree(value);
    }

    private void initSecurity() {
        this.gson = new Gson();
    }

    public record PacketData(String id, String payload) {
    }
}
