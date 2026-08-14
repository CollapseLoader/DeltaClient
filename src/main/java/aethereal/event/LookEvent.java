package aethereal.event;

import aethereal.core.Event;


public class LookEvent extends Event {
    public final double yaw;
    public final double pitch;

    public LookEvent(double yaw, double pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getYaw() {
        return this.yaw;
    }

    public double c() {
        return this.pitch;
    }
}
