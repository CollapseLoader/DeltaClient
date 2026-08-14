package aethereal.event;

import aethereal.core.Event;


public class RatioEvent extends Event {
    private float ratio;

    public RatioEvent(float ratio) {
        this.ratio = ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float b() {
        return this.ratio;
    }
}
