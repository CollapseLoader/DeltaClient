package aethereal.event;

import aethereal.core.Event;


public class ClickEvent extends Event {
    private final a type;
    private final double mouseX;
    private final double mouseY;
    private final int button;

    public ClickEvent(double mouseX, double mouseY, int button, a type) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.button = button;
        this.type = type;
    }

    public a getType() {
        return this.type;
    }

    public double getMouseX() {
        return this.mouseX;
    }

    public double getMouseY() {
        return this.mouseY;
    }

    public int h() {
        return this.button;
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
