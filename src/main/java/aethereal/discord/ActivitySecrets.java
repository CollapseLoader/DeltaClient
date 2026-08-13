package aethereal.discord;


import com.google.gson.JsonObject;

import java.util.Optional;

public final class ActivitySecrets {
    private final String a;
    private final String b;
    private final String c;

    public ActivitySecrets(String join, String spectate, String match) {
        this.a = join;
        this.b = spectate;
        this.c = match;
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

    public JsonObject a() {
        JsonObject json = new JsonObject();
        Optional.ofNullable(this.a).ifPresent(v -> {
            json.addProperty("join", v);
        });
        Optional.ofNullable(this.b).ifPresent(v2 -> {
            json.addProperty("spectate", v2);
        });
        Optional.ofNullable(this.c).ifPresent(v3 -> {
            json.addProperty("match", v3);
        });
        return json;
    }
}
