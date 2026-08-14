package aethereal.event;

import aethereal.core.Event;


public class DropItemEvent extends Event {
    private final int slot;

    public DropItemEvent(int slot) {
        this.slot = slot;
    }

    public int b() {
        return this.slot;
    }
}
