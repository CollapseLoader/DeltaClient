package aethereal.util;

public final class StringUtils {
    public static final String a = "";
    public static final String d = "\n";

    private StringUtils() {
    }

    public static String e(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    public static String w(String value) {
        return value;
    }
}
