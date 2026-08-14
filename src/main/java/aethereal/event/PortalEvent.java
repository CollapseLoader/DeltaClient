package aethereal.event;

import aethereal.core.Event;


public class PortalEvent extends Event {
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
