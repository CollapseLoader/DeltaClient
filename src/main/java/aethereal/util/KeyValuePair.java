package aethereal.util;


public record KeyValuePair(String a, Object b) {

    public String toString() {
        return this.a + "=\"" + this.b + "\"";
    }
}
