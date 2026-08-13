package baritone.api.pathing.goals;

import net.minecraft.util.math.BlockPos;

public record GoalBlock(BlockPos pos) implements Goal {
    public GoalBlock(BlockPos pos) {
        this.pos = pos;
    }
}
