package aethereal.discord;


import com.google.gson.JsonObject;

import java.util.Optional;

public final class ActivityAssets {
    private final String a;
    private final String b;
    private final String c;
    private final String d;

    public ActivityAssets(String largeImage, String largeText, String smallImage, String smallText) {
        this.a = largeImage;
        this.b = largeText;
        this.c = smallImage;
        this.d = smallText;
    }

    public String b() {
        return this.a;
    }

    public String c() {
        return this.b;
    }

    public String d() {
        return this.c;
    }

    public String e() {
        return this.d;
    }

    public JsonObject a() {
        JsonObject json = new JsonObject();
        Optional.ofNullable(this.a).ifPresent(v -> {
            json.addProperty("large_image", v);
        });
        Optional.ofNullable(this.b).ifPresent(v2 -> {
            json.addProperty("large_text", v2);
        });
        Optional.ofNullable(this.c).ifPresent(v3 -> {
            json.addProperty("small_image", v3);
        });
        Optional.ofNullable(this.d).ifPresent(v4 -> {
            json.addProperty("small_text", v4);
        });
        return json;
    }
}
