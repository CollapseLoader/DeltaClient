package aethereal.event;

import aethereal.core.Event;


public class LookEvent extends Event {
    public final double a;
    public final double b;

    public LookEvent(double yaw, double pitch) {
        this.a = yaw;
        this.b = pitch;
    }

    public double b() {
        return this.a;
    }

    public double c() {
        return this.b;
    }
}
