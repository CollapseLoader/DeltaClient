package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class WillLandEvent extends Event implements IEvent {
    private final boolean a;

    @Generated
    public WillLandEvent(boolean willLand) {
        this.a = willLand;
    }

    @Generated
    public boolean b() {
        return this.a;
    }
}
