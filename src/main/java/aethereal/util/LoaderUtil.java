package aethereal.util;

public final class LoaderUtil {
    private LoaderUtil() {
    }

    public static Class<?> b(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }
}
