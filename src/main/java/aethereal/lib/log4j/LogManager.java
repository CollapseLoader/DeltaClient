package aethereal.lib.log4j;

public final class LogManager {
    private LogManager() {
    }

    public static Logger b(Class<?> type) {
        return new Logger(type);
    }
}
