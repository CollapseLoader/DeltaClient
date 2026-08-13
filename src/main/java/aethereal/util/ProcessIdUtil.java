package aethereal.util;

public final class ProcessIdUtil {
    public static final String a = "✓";

    private ProcessIdUtil() {
    }

    public static long a() {
        return ProcessHandle.current().pid();
    }
}
