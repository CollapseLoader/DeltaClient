package aethereal.lib.log4j;

public final class LoggerFactory {
    private LoggerFactory() {
    }

    public static Logger_2 a(Class<?> type) {
        return new Logger_2(type);
    }
}
