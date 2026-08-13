package aethereal.macro;


import lombok.Generated;

public class MacrosConstructor {
    private String a;
    private String b;

    @Generated
    public MacrosConstructor(String key, String command) {
        this.a = key;
        this.b = command;
    }

    @Generated
    public void a(String key) {
        this.a = key;
    }

    @Generated
    public void b(String command) {
        this.b = command;
    }

    @Generated
    public String a() {
        return this.a;
    }

    @Generated
    public String b() {
        return this.b;
    }
}
