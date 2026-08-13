package aethereal.module.movement;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import net.minecraft.item.BlockItem;

@ModuleRegister(a = "No Delay", b = "Убирает задержку у выбранных действий", c = Category.Movement)
public class NoDelay extends Module {
    public final MultiModeSetting b = new MultiModeSetting("Отключить задержку на", new BooleanSetting("Поставку блоков", true), new BooleanSetting("Прыжки", true));

    public NoDelay() {
        a(this.b);
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.b.a("Поставку блоков").c().booleanValue() && (aM_.player.getMainHandStack().getItem() instanceof BlockItem) && !Delta.h().d().t().aS().m()) {
            ((platform.inject.accessors.MinecraftClientAccessor) aM_).setItemUseCooldown(0);
        }
        if (this.b.a("Прыжки").c().booleanValue()) {
            ((platform.inject.accessors.LivingEntityAccessor) aM_.player).setJumpingCooldown(0);
        }
    }
}
