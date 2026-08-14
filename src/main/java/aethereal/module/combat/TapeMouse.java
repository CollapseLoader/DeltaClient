package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;

@ModuleRegister(name = "Tape Mouse", description = "Автоматически кликает выбранной кнопкой мыши через заданные промежутки времени", category = Category.Combat)
public class TapeMouse extends Module {
    private final SliderSetting b = new SliderSetting("Задержка между кликами", 1000.0f, 10.0f, 5000.0f, 10.0f);
    private final BooleanSetting c = new BooleanSetting("Не кликать во время еды", true);
    private final ModeSetting d = new ModeSetting("Кнопка мыши", "Правая", "Правая", "Левая");
    private final CounterUtil e = new CounterUtil();

    public TapeMouse() {
        a(this.d, this.c, this.b);
    }

    public SliderSetting q() {
        return this.b;
    }

    public BooleanSetting r() {
        return this.c;
    }

    public ModeSetting s() {
        return this.d;
    }

    public CounterUtil t() {
        return this.e;
    }

    @EventTarget
    public void a(TickEvent event) {
        if ((!this.c.c().booleanValue() || !mc.player.isUsingItem()) && this.e.a(this.b.c().intValue())) {
            switch (this.d.c()) {
                case "Правая":
                    ((platform.inject.invokers.MinecraftClientInvoker) mc).invokeDoItemUse();
                    break;
                case "Левая":
                    ((platform.inject.invokers.MinecraftClientInvoker) mc).invokeDoAttack();
                    break;
            }
            this.e.b();
        }
    }
}
