package aethereal.event;

import aethereal.core.Event;



public class ScrollEvent extends Event {
    private final double a;
    private final double b;

    public ScrollEvent(double horizontal, double vertical) {
        this.a = horizontal;
        this.b = vertical;
    }

    public double b() {
        return this.a;
    }

    public double c() {
        return this.b;
    }
}
