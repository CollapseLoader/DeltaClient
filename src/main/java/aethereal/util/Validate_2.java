package aethereal.util;

public final class Validate_2 {
    private Validate_2() {
    }

    public static void a(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
    }

    public static void a(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException("invalid argument");
        }
    }

    public static void a(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }
}
