package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import net.minecraft.client.gui.screen.DeathScreen;

@ModuleRegister(a = "Auto Respawn", b = "Автоматически возрождает персонажа после смерти", c = Category.Player)
public class AutoRespawn extends Module {
    @EventTarget
    public void a(TickEvent event) {
        if ((aM_.currentScreen instanceof DeathScreen) && aM_.player.deathTime >= 5) {
            aM_.player.requestRespawn();
        }
    }
}
