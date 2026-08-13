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
        if (aM_.player.deathTime == 1) {
            ChatUtil.a(String.format("Вы погибли на координатах: &c[%d, %d, %d]", Integer.valueOf(aM_.player.getBlockPos().getX()), Integer.valueOf(aM_.player.getBlockPos().getY()), Integer.valueOf(aM_.player.getBlockPos().getZ())));
        }
    }
}
