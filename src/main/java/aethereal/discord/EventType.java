package aethereal.discord;


import java.util.Optional;

public enum EventType {
    READY("READY", false),
    ERROR("ERROR", false),
    ACTIVITY_JOIN("ACTIVITY_JOIN", true),
    ACTIVITY_SPECTATE("ACTIVITY_SPECTATE", true),
    ACTIVITY_JOIN_REQUEST("ACTIVITY_JOIN_REQUEST", true);

    private final String f;
    private final boolean g;

    EventType(String value, boolean subscribable) {
        this.f = value;
        this.g = subscribable;
    }

    public static Optional<EventType> a(String value) {
        for (EventType t : values()) {
            if (t.f.equals(value)) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    public String a() {
        return this.f;
    }

    public boolean b() {
        return this.g;
    }
}
