package aethereal.event;

import aethereal.core.Event;


public class WillLandEvent extends Event {
    private final boolean a;

    public WillLandEvent(boolean willLand) {
        this.a = willLand;
    }

    public boolean b() {
        return this.a;
    }
}
