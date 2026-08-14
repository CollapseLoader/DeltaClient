package aethereal.discord;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Optional;

public final class ActivityParty {
    public static final int privacyUnknown = 0;
    public static final int privacyPublic = 1;
    private final String id;
    private final int currentSize;
    private final int maxSize;
    private final Integer privacy;

    public ActivityParty(String id, int currentSize, int maxSize, Integer privacy) {
        this.id = id;
        this.currentSize = currentSize;
        this.maxSize = maxSize;
        this.privacy = privacy;
    }

    public static ActivityParty a(String id, int currentSize, int maxSize) {
        return a(id, currentSize, maxSize, null);
    }

    public static ActivityParty a(String id, int currentSize, int maxSize, Integer privacy) {
        if (currentSize < 0) {
            throw new IllegalArgumentException("currentSize must be non-negative, got " + currentSize);
        }
        if (maxSize < 0) {
            throw new IllegalArgumentException("maxSize must be non-negative, got " + maxSize);
        }
        if (currentSize > maxSize) {
            throw new IllegalArgumentException("currentSize (" + currentSize + ") must be <= maxSize (" + maxSize + ")");
        }
        return new ActivityParty(id, currentSize, maxSize, privacy);
    }

    public String b() {
        return this.id;
    }

    public int c() {
        return this.currentSize;
    }

    public int d() {
        return this.maxSize;
    }

    public Integer e() {
        return this.privacy;
    }

    public JsonObject a() {
        JsonObject json = new JsonObject();
        Optional.ofNullable(this.id).ifPresent(v -> {
            json.addProperty("id", v);
        });
        if (this.maxSize > 0) {
            JsonArray sizeArr = new JsonArray();
            sizeArr.add(new JsonPrimitive(Integer.valueOf(this.currentSize)));
            sizeArr.add(new JsonPrimitive(Integer.valueOf(this.maxSize)));
            json.add("size", sizeArr);
        }
        Optional.ofNullable(this.privacy).ifPresent(p -> {
            json.addProperty("privacy", p);
        });
        return json;
    }
}
