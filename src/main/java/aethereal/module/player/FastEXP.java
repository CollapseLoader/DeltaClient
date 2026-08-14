package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import net.minecraft.item.Items;

@ModuleRegister(name = "Fast EXP", description = "Позволяет очень быстро бросать опыт", category = Category.Player)
public class FastEXP extends Module {
    @EventTarget
    public void a(TickEvent event) {
        if (mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE) {
            ((platform.inject.accessors.MinecraftClientAccessor) mc).setItemUseCooldown(0);
        }
    }
}
