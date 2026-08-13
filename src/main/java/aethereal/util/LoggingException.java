package aethereal.util;

public class LoggingException extends RuntimeException {
    public LoggingException(String message) {
        super(message);
    }

    public LoggingException(String message, Throwable cause) {
        super(message, cause);
    }
}
