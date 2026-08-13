package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;

public class PortalEvent extends Event implements IEvent {
    private boolean a;

    @Generated
    public PortalEvent(boolean inPortal) {
        this.a = inPortal;
    }

    @Generated
    public void b(boolean inPortal) {
        this.a = inPortal;
    }

    @Generated
    public boolean b() {
        return this.a;
    }
}
