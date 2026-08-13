package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import net.minecraft.entity.effect.StatusEffects;

@ModuleRegister(a = "Sprint", b = "Автоматически включает спринт при движении", c = Category.Movement)
public class Sprint extends Module {
    @EventTarget
    public void a(TickEvent event) {
        aM_.player.setSprinting(aM_.player.input.movementForward > 0.0f && !aM_.player.hasStatusEffect(StatusEffects.BLINDNESS) && (aM_.player.getAbilities().invulnerable || aM_.player.getHungerManager().getFoodLevel() > 6));
    }
}
