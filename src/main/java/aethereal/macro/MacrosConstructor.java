package aethereal.macro;


public class MacrosConstructor {
    private String key;
    private String command;

    public MacrosConstructor(String key, String command) {
        this.key = key;
        this.command = command;
    }

    public void a(String key) {
        this.key = key;
    }

    public void b(String command) {
        this.command = command;
    }

    public String a() {
        return this.key;
    }

    public String b() {
        return this.command;
    }
}
