package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;
import net.minecraft.util.math.BlockPos;

public class PotionEvent extends Event implements IEvent {
    private final a a;
    private final int b;
    private final BlockPos c;

    @Generated
    public PotionEvent(a type, int data, BlockPos pos) {
        this.a = type;
        this.b = data;
        this.c = pos;
    }

    @Generated
    public a b() {
        return this.a;
    }

    @Generated
    public int c() {
        return this.b;
    }

    @Generated
    public BlockPos d() {
        return this.c;
    }

    public enum a {
        PARTICLES
    }
}
