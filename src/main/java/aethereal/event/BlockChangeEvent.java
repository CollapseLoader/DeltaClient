package aethereal.event;

import aethereal.core.Event;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockChangeEvent extends Event {
    private final BlockPos a;
    private final BlockState b;
    private final BlockState c;

    public BlockChangeEvent(BlockPos pos, BlockState oldState, BlockState state) {
        this.a = pos;
        this.b = oldState;
        this.c = state;
    }

    public BlockPos b() {
        return this.a;
    }

    public BlockState c() {
        return this.b;
    }

    public BlockState d() {
        return this.c;
    }
}
