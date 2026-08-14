package baritone.api;

import baritone.api.pathing.goals.Goal;
import lombok.Setter;

public final class BaritoneAPI {
    private static final BaritoneSettings settings = new BaritoneSettings();

    public static BaritoneSettings getSettings() {
        return settings;
    }
    private static final IBaritone noop = new NoopBaritone();
    @Setter
    private static IBaritoneProvider provider;

    private BaritoneAPI() {
    }

    public static IBaritoneProvider getProvider() {
        if (provider == null) {
            return () -> noop;
        }
        return provider;
    }

    private static final class NoopBaritone implements IBaritone {
        private final PathingBehavior pathing = new PathingBehavior();
        private final CustomGoalProcess goals = new CustomGoalProcess();
        private final MineProcess mine = new MineProcess();

        @Override
        public PathingBehavior getPathingBehavior() {
            return pathing;
        }

        @Override
        public CustomGoalProcess getCustomGoalProcess() {
            return goals;
        }

        @Override
        public MineProcess getMineProcess() {
            return mine;
        }

        private static final class PathingBehavior implements IBaritone.PathingBehavior {
            @Override
            public boolean hasPath() {
                return false;
            }

            @Override
            public void cancelEverything() {
            }

            @Override
            public void requestPause() {
            }
        }

        private static final class CustomGoalProcess implements IBaritone.CustomGoalProcess {
            @Override
            public void setGoalAndPath(Goal goal) {
            }
        }

        private static final class MineProcess implements IBaritone.MineProcess {
            private final java.util.Set<net.minecraft.util.math.BlockPos> blacklist = new java.util.HashSet<>();

            @Override
            public boolean isActive() {
                return false;
            }

            @Override
            public void minePositions(net.minecraft.item.Item item, Iterable<net.minecraft.util.math.BlockPos> positions) {
            }

            @Override
            public java.util.Set<net.minecraft.util.math.BlockPos> getBlacklist() {
                return blacklist;
            }
        }
    }
}
