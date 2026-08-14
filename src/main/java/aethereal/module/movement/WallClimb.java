package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.MotionEvent;
import aethereal.setting.ModeSetting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;

@ModuleRegister(a = "Wall Climb", b = "Позволяет взбираться по стенам", c = Category.Movement)
public class WallClimb extends Module {
    private final ModeSetting b = new ModeSetting("Выберите тип обхода", "Матрикс", "Матрикс");
    private final SliderSetting c = new SliderSetting("Скорость режима", 20.0f, 1.0f, 100.0f, 1.0f);
    private final CounterUtil d = new CounterUtil();

    public WallClimb() {
        a(this.b, this.c);
    }

    @Override
    public void b() {
        super.b();
        this.d.b();
    }

    @EventTarget
    public void a(MotionEvent event) {
        if (this.b.l("Матрикс")) {
            a(event, this.c.c().longValue());
        }
    }

    private void a(MotionEvent event, long value) {
        if (this.d.a(value * 5) && mc.player.horizontalCollision) {
            event.b(true);
            mc.player.setOnGround(true);
            mc.player.verticalCollision = true;
            mc.player.horizontalCollision = true;
            mc.player.jump();
            this.d.b();
        }
    }
}
