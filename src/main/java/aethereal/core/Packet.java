package aethereal.core;

import aethereal.network.PacketSecurity;
import lombok.Generated;

public class Packet {
    private final PacketSecurity security;
    private final String id;
    private String payload;

    public Packet(String id, String payload, PacketSecurity security) {
        this.id = id;
        this.payload = payload;
        this.security = security;
    }

    @Generated
    public PacketSecurity getSecurity() {
        return this.security;
    }

    @Generated
    public String getId() {
        return this.id;
    }

    @Generated
    public String getPayload() {
        return this.payload;
    }

    @Generated
    public void setPayload(String payload) {
        this.payload = payload;
    }
}
