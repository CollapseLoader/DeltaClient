package aethereal.util;

public final class ObjectUtils {
    private ObjectUtils() {
    }

    public static <T> T a(T primary, T fallback) {
        return primary != null ? primary : fallback;
    }
}
