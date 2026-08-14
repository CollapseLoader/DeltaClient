package aethereal.event;

import aethereal.core.Event;



public class GammaEvent extends Event {
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
