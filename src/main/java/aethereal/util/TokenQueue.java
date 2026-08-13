package aethereal.util;

public final class TokenQueue {
    private String remaining;

    public TokenQueue(String value) {
        this.remaining = value == null ? "" : value;
    }

    public String g(String delimiter) {
        int index = remaining.indexOf(delimiter);
        if (index < 0) {
            String value = remaining;
            remaining = "";
            return value;
        }
        String value = remaining.substring(0, index);
        remaining = remaining.substring(index + delimiter.length());
        return value;
    }

    public String e(String delimiter) {
        int index = remaining.indexOf(delimiter);
        if (index < 0) {
            String value = remaining;
            remaining = "";
            return value;
        }
        String value = remaining.substring(0, index);
        remaining = remaining.substring(index + delimiter.length());
        return value;
    }
}
