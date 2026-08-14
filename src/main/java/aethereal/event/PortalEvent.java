package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;


public class PortalEvent extends Event implements IEvent {
    private boolean a;

    public PortalEvent(boolean inPortal) {
        this.a = inPortal;
    }

    public void b(boolean inPortal) {
        this.a = inPortal;
    }

    public boolean b() {
        return this.a;
    }
}
