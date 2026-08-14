package aethereal.discord;


public final class FailureInfo {
    private final String type;
    private final String message;

    public FailureInfo(String type, String message) {
        String type2 = (type == null || type.isBlank()) ? "unknown" : type;
        String message2 = message == null ? "" : message;
        this.type = type2;
        this.message = message2;
    }

    public static FailureInfo a(Throwable throwable) {
        if (throwable == null) {
            return new FailureInfo("unknown", "");
        }
        String message = throwable.getMessage();
        if (message == null || message.isBlank()) {
            message = throwable.toString();
        }
        return new FailureInfo(throwable.getClass().getName(), message);
    }

    public String a() {
        return this.type;
    }

    public String b() {
        return this.message;
    }
}
