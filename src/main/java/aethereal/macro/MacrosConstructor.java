package aethereal.macro;


import lombok.Generated;

public class MacrosConstructor {
    private String key;
    private String command;

    @Generated
    public MacrosConstructor(String key, String command) {
        this.key = key;
        this.command = command;
    }

    @Generated
    public void a(String key) {
        this.key = key;
    }

    @Generated
    public void b(String command) {
        this.command = command;
    }

    @Generated
    public String a() {
        return this.key;
    }

    @Generated
    public String b() {
        return this.command;
    }
}
