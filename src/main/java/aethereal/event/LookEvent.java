package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class LookEvent extends Event implements IEvent {
    public final double a;
    public final double b;

    @Generated
    public LookEvent(double yaw, double pitch) {
        this.a = yaw;
        this.b = pitch;
    }

    @Generated
    public double b() {
        return this.a;
    }

    @Generated
    public double c() {
        return this.b;
    }
}
