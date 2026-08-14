package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.SliderSetting;
import net.minecraft.util.hit.BlockHitResult;

@ModuleRegister(a = "Fast Break", b = "Ускоряет разрушение блоков, обрабатывая добычу несколько раз за тик", c = Category.Movement)
public class FastBreak extends Module {
    private final SliderSetting b = new SliderSetting("Интенсивность копания", 2.0f, 1.0f, 5.0f, 0.25f);

    public FastBreak() {
        a(this.b);
    }

    @EventTarget
    public void a(TickEvent event) {
        if (mc.options.attackKey.isPressed() && mc.interactionManager.isBreakingBlock()) {
            if (mc.crosshairTarget instanceof BlockHitResult hit) {
                for (int i = 0; i < this.b.c().intValue() - 1; i++) {
                    mc.interactionManager.updateBlockBreakingProgress(hit.getBlockPos(), hit.getSide());
                }
            }
        }
    }
}
