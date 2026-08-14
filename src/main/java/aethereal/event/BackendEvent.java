package aethereal.event;

import aethereal.core.Event;


import aethereal.core.Packet;

public class BackendEvent extends Event {
    private final Packet packet;
    private final Phase phase;

    public BackendEvent(Packet packet, Phase type) {
        this.packet = packet;
        this.phase = type;
    }

    public BackendEvent(Phase type) {
        this.phase = type;
        this.packet = null;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public Phase getPhase() {
        return this.phase;
    }

    public boolean isReceive() {
        return this.phase == Phase.RECEIVE;
    }

    public boolean isClose() {
        return this.phase == Phase.CLOSE;
    }

    public enum Phase {
        RECEIVE,
        CLOSE
    }
}
