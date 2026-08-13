package aethereal.util;


import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class RuntimeEnvironment {
    @Deprecated
    public RuntimeEnvironment() {
    }

    private static boolean b(String path) {
        return Files.exists(Paths.get(path));
    }

    public static Boolean a() {
        return Boolean.valueOf(a(""));
    }

    static boolean a(String dirPrefix) {
        String value = a(dirPrefix + "/proc/1/environ", "container");
        if (value != null) {
            return !value.isEmpty();
        }
        return b(dirPrefix + "/.dockerenv") || b(dirPrefix + "/run/.containerenv");
    }

    private static String a(String envVarFile, String key) {
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(envVarFile));
            String content = new String(bytes, Charset.defaultCharset());
            String[] lines = content.split(String.valueOf((char) 0));
            String prefix = key + "=";
            return Arrays.stream(lines).filter(line -> {
                return line.startsWith(prefix);
            }).map(line2 -> {
                return line2.split("=", 2);
            }).map(keyValue -> {
                return keyValue[1];
            }).findFirst().orElse(null);
        } catch (IOException e) {
            return null;
        }
    }
}
