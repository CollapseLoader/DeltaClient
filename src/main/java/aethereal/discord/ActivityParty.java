package aethereal.discord;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Optional;

public final class ActivityParty {
    public static final int a = 0;
    public static final int b = 1;
    private final String c;
    private final int d;
    private final int e;
    private final Integer f;

    public ActivityParty(String id, int currentSize, int maxSize, Integer privacy) {
        this.c = id;
        this.d = currentSize;
        this.e = maxSize;
        this.f = privacy;
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
        return this.c;
    }

    public int c() {
        return this.d;
    }

    public int d() {
        return this.e;
    }

    public Integer e() {
        return this.f;
    }

    public JsonObject a() {
        JsonObject json = new JsonObject();
        Optional.ofNullable(this.c).ifPresent(v -> {
            json.addProperty("id", v);
        });
        if (this.e > 0) {
            JsonArray sizeArr = new JsonArray();
            sizeArr.add(new JsonPrimitive(Integer.valueOf(this.d)));
            sizeArr.add(new JsonPrimitive(Integer.valueOf(this.e)));
            json.add("size", sizeArr);
        }
        Optional.ofNullable(this.f).ifPresent(p -> {
            json.addProperty("privacy", p);
        });
        return json;
    }
}
