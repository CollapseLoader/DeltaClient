package aethereal.discord;


import java.util.Locale;

public enum Platform {
    WINDOWS,
    MACOS,
    LINUX;

    public static final Platform d = a();

    private static Platform a() {
        String os = System.getProperty(SystemProperties.cl, "").toLowerCase(Locale.ROOT);
        if (os.contains("win")) {
            return WINDOWS;
        }
        if (os.contains("mac") || os.contains("darwin")) {
            return MACOS;
        }
        return LINUX;
    }
}
