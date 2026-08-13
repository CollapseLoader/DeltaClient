package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class HandEvent extends Event implements IEvent {
    private final a type;

    @Generated
    public HandEvent(a phase) {
        this.type = phase;
    }

    @Generated
    public a d() {
        return this.type;
    }

    public boolean b() {
        return this.type == HandEvent.a.PRE;
    }

    public boolean c() {
        return this.type == HandEvent.a.POST;
    }

    public enum a {
        PRE,
        POST
    }
}
