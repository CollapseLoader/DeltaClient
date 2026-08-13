package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class RatioEvent extends Event implements IEvent {
    private float a;

    @Generated
    public RatioEvent(float ratio) {
        this.a = ratio;
    }

    @Generated
    public void a(float ratio) {
        this.a = ratio;
    }

    @Generated
    public float b() {
        return this.a;
    }
}
