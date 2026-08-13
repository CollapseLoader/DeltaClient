package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class DropItemEvent extends Event implements IEvent {
    private final int a;

    @Generated
    public DropItemEvent(int slot) {
        this.a = slot;
    }

    @Generated
    public int b() {
        return this.a;
    }
}
