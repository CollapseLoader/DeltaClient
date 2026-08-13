package aethereal.lib.javassist;

public class BadBytecode extends Exception {
    public BadBytecode(String message) {
        super(message);
    }

    public BadBytecode(String message, Throwable cause) {
        super(message, cause);
    }
}
