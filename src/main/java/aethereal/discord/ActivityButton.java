package aethereal.discord;


import aethereal.util.UrlValidator;

import com.google.gson.JsonObject;

public final class ActivityButton {
    private final String a;
    private final String b;

    public ActivityButton(String label, String url) {
        if (label == null || label.isEmpty() || label.length() > 32) {
            throw new IllegalArgumentException("Button label must be 1-32 characters, got: " + (label == null ? "null" : Integer.valueOf(label.length())));
        }
        if (url == null || url.length() > 256) {
            throw new IllegalArgumentException("Button URL must be at most 256 characters");
        }
        UrlValidator.a(url, "Button URL", 256);
        this.a = label;
        this.b = url;
    }

    public String b() {
        return this.a;
    }

    public String c() {
        return this.b;
    }

    public JsonObject a() {
        JsonObject json = new JsonObject();
        json.addProperty("label", this.a);
        json.addProperty("url", this.b);
        return json;
    }
}
