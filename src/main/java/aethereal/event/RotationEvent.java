package aethereal.event;

import aethereal.core.Event;


public class RotationEvent extends Event {
    public float a;
    public float b;

    public RotationEvent(float yaw, float pitch) {
        this.a = yaw;
        this.b = pitch;
    }

    public void a(float yaw) {
        this.a = yaw;
    }

    public void b(float pitch) {
        this.b = pitch;
    }

    public float b() {
        return this.a;
    }

    public float c() {
        return this.b;
    }
}
