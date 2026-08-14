package aethereal.discord;


import com.google.gson.JsonObject;

import java.util.Optional;

public final class ActivityTimestamps {
    private final Long start;
    private final Long end;

    public ActivityTimestamps(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    public static ActivityTimestamps a(long epochSeconds) {
        return new ActivityTimestamps(Long.valueOf(epochSeconds), null);
    }

    public static ActivityTimestamps b(long epochSeconds) {
        return new ActivityTimestamps(null, Long.valueOf(epochSeconds));
    }

    public Long b() {
        return this.start;
    }

    public Long c() {
        return this.end;
    }

    public JsonObject a() {
        JsonObject json = new JsonObject();
        Optional.ofNullable(this.start).ifPresent(s -> {
            json.addProperty("start", s);
        });
        Optional.ofNullable(this.end).ifPresent(e -> {
            json.addProperty("end", e);
        });
        return json;
    }
}
