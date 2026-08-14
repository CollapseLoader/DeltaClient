package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;

@ModuleRegister(a = "Death Coords", b = "Выводит координаты последней смерти", c = Category.Player)
public class DeathCoords extends Module {
    @EventTarget
    public void a(TickEvent event) {
        if (mc.player.deathTime == 1) {
            ChatUtil.sendMessage(String.format("Вы погибли на координатах: &c[%d, %d, %d]", Integer.valueOf(mc.player.getBlockPos().getX()), Integer.valueOf(mc.player.getBlockPos().getY()), Integer.valueOf(mc.player.getBlockPos().getZ())));
        }
    }
}
