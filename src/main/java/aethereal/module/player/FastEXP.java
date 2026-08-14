package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import net.minecraft.item.Items;

@ModuleRegister(a = "Fast EXP", b = "Позволяет очень быстро бросать опыт", c = Category.Player)
public class FastEXP extends Module implements Interface {
    @EventTarget
    public void a(TickEvent event) {
        if (mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE) {
            ((platform.inject.accessors.MinecraftClientAccessor) mc).setItemUseCooldown(0);
        }
    }
}
