package aethereal.event;

import aethereal.core.Event;


public class DropItemEvent extends Event {
    private final int a;

    public DropItemEvent(int slot) {
        this.a = slot;
    }

    public int b() {
        return this.a;
    }
}
