package aethereal.event;

import aethereal.core.Event;



public class HotbarEvent extends Event {
    private final int a;

    public HotbarEvent(int slot) {
        this.a = slot;
    }

    public int b() {
        return this.a;
    }
}
