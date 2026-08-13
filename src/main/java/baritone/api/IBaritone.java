package baritone.api;

import baritone.api.pathing.goals.Goal;
import net.minecraft.util.math.BlockPos;

public interface IBaritone {
    PathingBehavior getPathingBehavior();

    CustomGoalProcess getCustomGoalProcess();

    MineProcess getMineProcess();

    interface PathingBehavior {
        boolean hasPath();

        void cancelEverything();

        void requestPause();
    }

    interface CustomGoalProcess {
        void setGoalAndPath(Goal goal);
    }

    interface MineProcess {
        boolean isActive();

        void minePositions(net.minecraft.item.Item item, Iterable<BlockPos> positions);

        java.util.Set<BlockPos> getBlacklist();
    }
}
