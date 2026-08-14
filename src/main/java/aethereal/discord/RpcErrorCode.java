package aethereal.discord;


import aethereal.lib.javassist.CloseFrame;

public enum RpcErrorCode {
    UNKNOWN_ERROR(CloseFrame.a, "Unknown error"),
    INVALID_PAYLOAD(4000, "Invalid payload"),
    INVALID_COMMAND(4002, "Invalid command"),
    INVALID_EVENT(4004, "Invalid event"),
    INVALID_CHANNEL(4005, "Invalid channel"),
    INVALID_PERMISSIONS(4006, "Invalid permissions"),
    INVALID_CLIENT_ID(4007, "Invalid client ID"),
    INVALID_ORIGIN(4008, "Invalid origin"),
    INVALID_USER(4010, "Invalid user");

    private final int j;
    private final String k;

    RpcErrorCode(int code, String description) {
        this.j = code;
        this.k = description;
    }

    public static RpcErrorCode a(int code) {
        for (RpcErrorCode e : values()) {
            if (e.j == code) {
                return e;
            }
        }
        return UNKNOWN_ERROR;
    }

    public int a() {
        return this.j;
    }

    public String b() {
        return this.k;
    }
}
