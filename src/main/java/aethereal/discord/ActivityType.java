package aethereal.discord;


public enum ActivityType {
    PLAYING(0),
    STREAMING(1),
    LISTENING(2),
    WATCHING(3),
    COMPETING(5);

    private final int f;

    ActivityType(int value) {
        this.f = value;
    }

    public static ActivityType a(int value) {
        for (ActivityType t : values()) {
            if (t.f == value) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown activity type: " + value);
    }

    public int a() {
        return this.f;
    }
}
