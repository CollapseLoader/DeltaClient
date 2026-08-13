package baritone.api;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class BaritoneSettings {
    public final Setting<Boolean> blockFreeLook = new Setting<>(true);
    public final Setting<Boolean> allowBreak = new Setting<>(true);
    public final Setting<Boolean> allowPlace = new Setting<>(false);
    public final Setting<Boolean> avoidance = new Setting<>(false);
    public final Setting<Boolean> creative = new Setting<>(false);
    public final Setting<Float> turnSpeed = new Setting<>(60.0f);
    public final Setting<Double> randomLooking = new Setting<>(1.0d);
    public final Setting<Double> randomLooking113 = new Setting<>(1.0d);
    public final Setting<Integer> maxFallHeightNoWater = new Setting<>(3);
    public final Setting<List<Block>> blocksToAvoid = new Setting<>(new ArrayList<>());

    public static final class Setting<T> {
        public T value;

        public Setting(T value) {
            this.value = value;
        }
    }
}
