package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class GammaEvent extends Event implements IEvent {
    private double a;

    @Generated
    public GammaEvent(double gamma) {
        this.a = gamma;
    }

    @Generated
    public void a(double gamma) {
        this.a = gamma;
    }

    @Generated
    public double b() {
        return this.a;
    }
}
