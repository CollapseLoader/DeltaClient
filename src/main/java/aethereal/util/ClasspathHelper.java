package aethereal.util;

import java.util.Arrays;
import java.util.List;

public final class ClasspathHelper {
    private ClasspathHelper() {
    }

    public static Iterable<ClassLoader> a(ClassLoader... loaders) {
        if (loaders == null || loaders.length == 0) {
            return List.of(ClassLoader.getSystemClassLoader());
        }
        return Arrays.asList(loaders);
    }
}
