package aethereal.event;

import aethereal.core.Event;

public class HandEvent extends Event {
    private final eventPhase type;

    public HandEvent(eventPhase phase) {
        this.type = phase;
    }

    public eventPhase getType() {
        return this.type;
    }

    public boolean isPreEvent() {
        return this.type == HandEvent.eventPhase.PRE;
    }

    public boolean isPostEvent() {
        return this.type == HandEvent.eventPhase.POST;
    }

    public enum eventPhase {
        PRE,
        POST
    }
}
