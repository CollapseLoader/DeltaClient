package aethereal.event;

import aethereal.core.Event;


public class PushEvent extends Event {
    private final a a;

    public PushEvent(a type) {
        this.a = type;
    }

    public a b() {
        return this.a;
    }

    public enum a {
        BLOCKS,
        FLUIDS,
        ENTITIES,
        WORLD_BORDER,
        FISHING_HOOK
    }
}
