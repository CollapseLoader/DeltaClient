package aethereal.event;

import aethereal.core.Event;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class BlockChangeEvent extends Event {
    private final BlockPos pos;
    private final BlockState oldState;
    private final BlockState newState;

    public BlockChangeEvent(BlockPos pos, BlockState oldState, BlockState state) {
        this.pos = pos;
        this.oldState = oldState;
        this.newState = state;
    }

    public BlockPos b() {
        return this.pos;
    }

    public BlockState c() {
        return this.oldState;
    }

    public BlockState d() {
        return this.newState;
    }
}
