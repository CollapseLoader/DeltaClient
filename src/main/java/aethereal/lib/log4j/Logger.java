package aethereal.lib.log4j;

public class Logger {
    private final org.apache.logging.log4j.Logger delegate;

    public Logger(Class<?> type) {
        this.delegate = org.apache.logging.log4j.LogManager.getLogger(type);
    }

    public void a(String message, Object... args) {
        delegate.info(message, args);
    }

    public void d(String message, Object... args) {
        delegate.debug(message, args);
    }

    public void f(String message, Object... args) {
        delegate.error(message, args);
    }

    public void g(String message, Object... args) {
        delegate.warn(message, args);
    }

    public boolean isDebugEnabled() {
        return delegate.isDebugEnabled();
    }

    public void b(String message, Object... args) {
        delegate.warn(message, args);
    }

    public void b(String message, Throwable throwable) {
        delegate.warn(message, throwable);
    }
}
