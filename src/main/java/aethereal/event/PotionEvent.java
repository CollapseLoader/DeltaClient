package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import net.minecraft.util.math.BlockPos;

public class PotionEvent extends Event implements IEvent {
    private final a a;
    private final int b;
    private final BlockPos c;

    public PotionEvent(a type, int data, BlockPos pos) {
        this.a = type;
        this.b = data;
        this.c = pos;
    }

    public a b() {
        return this.a;
    }

    public int c() {
        return this.b;
    }

    public BlockPos d() {
        return this.c;
    }

    public enum a {
        PARTICLES
    }
}
