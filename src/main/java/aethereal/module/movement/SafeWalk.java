package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.InputEvent;

@ModuleRegister(a = "Safe Walk", b = "Не даёт упасть с края блоков", c = Category.Movement)
public class SafeWalk extends Module {
    @EventTarget
    public void a(InputEvent event) {
        b(event);
    }

    public void b(InputEvent event) {
        event.c(event.e() || (mc.world.getBlockState(mc.player.getBlockPos().down()).getCollisionShape(mc.world, mc.player.getBlockPos().down()).isEmpty() && mc.player.isOnGround()));
    }
}
