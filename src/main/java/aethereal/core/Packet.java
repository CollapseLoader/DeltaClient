package aethereal.core;

import aethereal.network.PacketSecurity;

public class Packet {
    private final PacketSecurity security;
    private final String id;
    private String payload;

    public Packet(String id, String payload, PacketSecurity security) {
        this.id = id;
        this.payload = payload;
        this.security = security;
    }

    public PacketSecurity getSecurity() {
        return this.security;
    }

    public String getId() {
        return this.id;
    }

    public String getPayload() {
        return this.payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
