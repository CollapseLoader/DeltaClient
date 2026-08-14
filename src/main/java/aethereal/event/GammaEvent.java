package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;


public class GammaEvent extends Event implements IEvent {
    private double a;

    public GammaEvent(double gamma) {
        this.a = gamma;
    }

    public void a(double gamma) {
        this.a = gamma;
    }

    public double b() {
        return this.a;
    }
}
