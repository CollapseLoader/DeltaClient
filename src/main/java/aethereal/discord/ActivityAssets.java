package aethereal.discord;


import com.google.gson.JsonObject;

import java.util.Optional;

public final class ActivityAssets {
    private final String largeImage;
    private final String largeText;
    private final String smallImage;
    private final String smallText;

    public ActivityAssets(String largeImage, String largeText, String smallImage, String smallText) {
        this.largeImage = largeImage;
        this.largeText = largeText;
        this.smallImage = smallImage;
        this.smallText = smallText;
    }

    public String b() {
        return this.largeImage;
    }

    public String c() {
        return this.largeText;
    }

    public String d() {
        return this.smallImage;
    }

    public String e() {
        return this.smallText;
    }

    public JsonObject a() {
        JsonObject json = new JsonObject();
        Optional.ofNullable(this.largeImage).ifPresent(v -> {
            json.addProperty("large_image", v);
        });
        Optional.ofNullable(this.largeText).ifPresent(v2 -> {
            json.addProperty("large_text", v2);
        });
        Optional.ofNullable(this.smallImage).ifPresent(v3 -> {
            json.addProperty("small_image", v3);
        });
        Optional.ofNullable(this.smallText).ifPresent(v4 -> {
            json.addProperty("small_text", v4);
        });
        return json;
    }
}
