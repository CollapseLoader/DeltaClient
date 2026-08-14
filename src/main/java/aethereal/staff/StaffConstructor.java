package aethereal.staff;


import aethereal.render.AnimationUtil;

public class StaffConstructor {
    private final AnimationUtil b = new AnimationUtil();
    private String a;

    public StaffConstructor() {
    }

    public StaffConstructor(String name) {
        this.a = name;
    }

    public void a(String name) {
        this.a = name;
    }

    public String a() {
        return this.a;
    }

    public AnimationUtil b() {
        return this.b;
    }
}
