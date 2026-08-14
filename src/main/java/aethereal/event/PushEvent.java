package aethereal.event;

import aethereal.core.Event;


public class PushEvent extends Event {
    private final type a;

    public PushEvent(type type) {
        this.a = type;
    }

    public type b() {
        return this.a;
    }

    public enum type {
        BLOCKS,
        FLUIDS,
        ENTITIES,
        WORLD_BORDER,
        FISHING_HOOK
    }
}
