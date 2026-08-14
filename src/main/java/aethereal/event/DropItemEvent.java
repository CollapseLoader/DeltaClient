package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;


public class DropItemEvent extends Event implements IEvent {
    private final int a;

    public DropItemEvent(int slot) {
        this.a = slot;
    }

    public int b() {
        return this.a;
    }
}
