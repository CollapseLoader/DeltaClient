package aethereal.event;

import aethereal.core.Event;


public class RatioEvent extends Event {
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
