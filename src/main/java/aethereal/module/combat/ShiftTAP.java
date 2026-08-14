package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.AttackEvent;
import aethereal.event.InputEvent;
import aethereal.event.TickEvent;
import net.minecraft.entity.player.PlayerEntity;

@ModuleRegister(name = "Shift TAP", description = "Автоматически приседает в момент удара по игроку", category = Category.Combat)
public class ShiftTAP extends Module {
    private int cooldownTicks;

    @EventTarget
    public void onAttack(AttackEvent event) {
        if (event.b() instanceof PlayerEntity) {
            this.cooldownTicks = 2;
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (this.cooldownTicks > 0) {
            this.cooldownTicks--;
        }
    }

    @EventTarget
    public void onInput(InputEvent event) {
        if (this.cooldownTicks > 0) {
            event.setSneak(true);
        }
    }
}
