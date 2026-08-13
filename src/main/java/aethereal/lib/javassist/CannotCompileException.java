package aethereal.lib.javassist;

public class CannotCompileException extends javassist.CannotCompileException {
    public CannotCompileException(String message) {
        super(message);
    }

    public CannotCompileException(String message, Throwable cause) {
        super(message, cause);
    }
}
