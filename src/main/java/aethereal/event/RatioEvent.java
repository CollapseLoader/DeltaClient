package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;


public class RatioEvent extends Event implements IEvent {
    private float a;

    public RatioEvent(float ratio) {
        this.a = ratio;
    }

    public void a(float ratio) {
        this.a = ratio;
    }

    public float b() {
        return this.a;
    }
}
