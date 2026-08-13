package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.SlowEvent;
import aethereal.setting.ModeSetting;

@ModuleRegister(a = "No Slow Down", b = "Убирает замедление при использовании предметов", c = Category.Movement)
public class NoSlowDown extends Module {
    private final ModeSetting b = new ModeSetting("Режим использования", "Vanilla", "Vanilla");

    public NoSlowDown() {
        a(this.b);
    }

    @EventTarget
    public void a(SlowEvent slow) {
        if (this.b.l("Vanilla")) {
            slow.a(true);
        }
    }
}
