package aethereal.event;

import aethereal.core.Event;


public class PortalEvent extends Event {
    private boolean inPortal;

    public PortalEvent(boolean inPortal) {
        this.inPortal = inPortal;
    }

    public void setInPortal(boolean inPortal) {
        this.inPortal = inPortal;
    }

    public boolean b() {
        return this.inPortal;
    }
}
