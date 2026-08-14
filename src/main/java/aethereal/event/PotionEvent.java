package aethereal.event;

import aethereal.core.Event;


import net.minecraft.util.math.BlockPos;

public class PotionEvent extends Event {
    private final type a;
    private final int data;
    private final BlockPos pos;

    public PotionEvent(type type, int data, BlockPos pos) {
        this.a = type;
        this.data = data;
        this.pos = pos;
    }

    public type b() {
        return this.a;
    }

    public int c() {
        return this.data;
    }

    public BlockPos d() {
        return this.pos;
    }

    public enum type {
        PARTICLES
    }
}
