package aethereal.event;

import aethereal.core.Event;


import net.minecraft.util.math.BlockPos;

public class PotionEvent extends Event {
    private final type type;
    private final int data;
    private final BlockPos pos;

    public PotionEvent(type type, int data, BlockPos pos) {
        this.type = type;
        this.data = data;
        this.pos = pos;
    }

    public type getType() {
        return this.type;
    }

    public int getData() {
        return this.data;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public enum type {
        PARTICLES
    }
}
