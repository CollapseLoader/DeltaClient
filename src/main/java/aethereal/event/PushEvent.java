package aethereal.event;

import aethereal.core.Event;


public class PushEvent extends Event {
    private final type type;

    public PushEvent(type type) {
        this.type = type;
    }

    public type b() {
        return this.type;
    }

    public enum type {
        BLOCKS,
        FLUIDS,
        ENTITIES,
        WORLD_BORDER,
        FISHING_HOOK
    }
}
