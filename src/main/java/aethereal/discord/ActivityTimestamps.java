package aethereal.discord;


import com.google.gson.JsonObject;

import java.util.Optional;

public final class ActivityTimestamps {
    private final Long a;
    private final Long b;

    public ActivityTimestamps(Long start, Long end) {
        this.a = start;
        this.b = end;
    }

    public static ActivityTimestamps a(long epochSeconds) {
        return new ActivityTimestamps(Long.valueOf(epochSeconds), null);
    }

    public static ActivityTimestamps b(long epochSeconds) {
        return new ActivityTimestamps(null, Long.valueOf(epochSeconds));
    }

    public Long b() {
        return this.a;
    }

    public Long c() {
        return this.b;
    }

    public JsonObject a() {
        JsonObject json = new JsonObject();
        Optional.ofNullable(this.a).ifPresent(s -> {
            json.addProperty("start", s);
        });
        Optional.ofNullable(this.b).ifPresent(e -> {
            json.addProperty("end", e);
        });
        return json;
    }
}
