package aethereal.staff;


import aethereal.render.AnimationUtil;
import lombok.Generated;

public class StaffConstructor {
    private final AnimationUtil b = new AnimationUtil();
    private String a;

    @Generated
    public StaffConstructor() {
    }

    public StaffConstructor(String name) {
        this.a = name;
    }

    @Generated
    public void a(String name) {
        this.a = name;
    }

    @Generated
    public String a() {
        return this.a;
    }

    @Generated
    public AnimationUtil b() {
        return this.b;
    }
}
