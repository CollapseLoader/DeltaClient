package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class RotationEvent extends Event implements IEvent {
    public float a;
    public float b;

    @Generated
    public RotationEvent(float yaw, float pitch) {
        this.a = yaw;
        this.b = pitch;
    }

    @Generated
    public void a(float yaw) {
        this.a = yaw;
    }

    @Generated
    public void b(float pitch) {
        this.b = pitch;
    }

    @Generated
    public float b() {
        return this.a;
    }

    @Generated
    public float c() {
        return this.b;
    }
}
