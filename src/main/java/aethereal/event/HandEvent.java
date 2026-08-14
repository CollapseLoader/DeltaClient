package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;


public class HandEvent extends Event implements IEvent {
    private final a type;

    public HandEvent(a phase) {
        this.type = phase;
    }

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
