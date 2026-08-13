package baritone.api.pathing.goals;

import net.minecraft.util.math.BlockPos;

public record GoalRunAway(double distance, BlockPos[] positions) implements Goal {
}
