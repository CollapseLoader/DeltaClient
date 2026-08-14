package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;


public class ScrollEvent extends Event implements IEvent {
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
