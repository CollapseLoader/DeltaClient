package aethereal.discord;


import com.google.gson.JsonObject;

import java.util.Optional;

public final class ActivitySecrets {
    private final String join;
    private final String spectate;
    private final String match;

    public ActivitySecrets(String join, String spectate, String match) {
        this.join = join;
        this.spectate = spectate;
        this.match = match;
    }

    public String b() {
        return this.join;
    }

    public String c() {
        return this.spectate;
    }

    public String d() {
        return this.match;
    }

    public JsonObject a() {
        JsonObject json = new JsonObject();
        Optional.ofNullable(this.join).ifPresent(v -> {
            json.addProperty("join", v);
        });
        Optional.ofNullable(this.spectate).ifPresent(v2 -> {
            json.addProperty("spectate", v2);
        });
        Optional.ofNullable(this.match).ifPresent(v3 -> {
            json.addProperty("match", v3);
        });
        return json;
    }
}
