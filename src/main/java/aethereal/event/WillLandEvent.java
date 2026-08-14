package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;


public class WillLandEvent extends Event implements IEvent {
    private final boolean a;

    public WillLandEvent(boolean willLand) {
        this.a = willLand;
    }

    public boolean b() {
        return this.a;
    }
}
