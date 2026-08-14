package aethereal.event;

import aethereal.core.Event;


public class ScrollEvent extends Event {
    private final double horizontal;
    private final double vertical;

    public ScrollEvent(double horizontal, double vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public double getHorizontal() {
        return this.horizontal;
    }

    public double c() {
        return this.vertical;
    }
}
