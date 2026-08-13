package aethereal.lib.javassist;

public enum OpCode {
    HANDSHAKE(0),
    FRAME(1),
    CLOSE(2),
    PING(3),
    PONG(4);

    private final int code;

    OpCode(int code) {
        this.code = code;
    }

    public static OpCode a(int code) {
        for (OpCode opCode : values()) {
            if (opCode.code == code) {
                return opCode;
            }
        }
        return null;
    }

    public int a() {
        return code;
    }
}
