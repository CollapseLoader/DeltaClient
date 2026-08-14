package aethereal.event;

import aethereal.core.Event;


public class ClickEvent extends Event {
    private final a type;
    private final double b;
    private final double c;
    private final int d;

    public ClickEvent(double mouseX, double mouseY, int button, a type) {
        this.b = mouseX;
        this.c = mouseY;
        this.d = button;
        this.type = type;
    }

    public a e() {
        return this.type;
    }

    public double f() {
        return this.b;
    }

    public double g() {
        return this.c;
    }

    public int h() {
        return this.d;
    }

    public boolean b() {
        return this.type == ClickEvent.a.PRESS;
    }

    public boolean c() {
        return this.type == ClickEvent.a.RELEASE;
    }

    public boolean d() {
        return this.type == ClickEvent.a.DRAG;
    }

    public enum a {
        PRESS,
        RELEASE,
        DRAG
    }
}
