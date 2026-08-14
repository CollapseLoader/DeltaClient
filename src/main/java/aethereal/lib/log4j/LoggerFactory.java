package aethereal.lib.log4j;

public final class LoggerFactory {
    private LoggerFactory() {
    }

    public static Logger a(Class<?> type) {
        return new Logger(type);
    }
}
