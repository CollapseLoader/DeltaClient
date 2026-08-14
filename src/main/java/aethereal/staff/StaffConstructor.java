package aethereal.staff;


import aethereal.render.AnimationUtil;

public class StaffConstructor {
    private final AnimationUtil b = new AnimationUtil();
    private String name;

    public StaffConstructor() {
    }

    public StaffConstructor(String name) {
        this.name = name;
    }

    public void a(String name) {
        this.name = name;
    }

    public String a() {
        return this.name;
    }

    public AnimationUtil b() {
        return this.b;
    }
}
