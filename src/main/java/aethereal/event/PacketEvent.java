package aethereal.event;

import aethereal.core.Event;


import net.minecraft.network.packet.Packet;

public class PacketEvent extends Event {
    private final Packet<?> packet;
    private final Type type;

    public PacketEvent(Packet<?> packet, Type type) {
        this.packet = packet;
        this.type = type;
    }

    public Packet<?> d() {
        return this.packet;
    }

    public Type e() {
        return this.type;
    }

    public boolean b() {
        return this.type == Type.SEND;
    }

    public boolean c() {
        return this.type == Type.RECEIVE;
    }

    public enum Type {
        SEND,
        RECEIVE
    }
}
