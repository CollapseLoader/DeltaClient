package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.SliderSetting;

@ModuleRegister(name = "Fly", description = "Позволяет свободно летать по миру", category = Category.Movement)
public class Fly extends Module {
    private final SliderSetting b = new SliderSetting("Скорость X и Z", 1.0f, 0.1f, 5.0f, 0.1f);
    private final SliderSetting c = new SliderSetting("Скорость Y", 1.0f, 0.1f, 5.0f, 0.1f);

    public Fly() {
        a(this.b, this.c);
    }

    @EventTarget
    public void a(TickEvent event) {
        q();
    }

    private void q() {
        double dFloatValue;
        float fSignum;
        float yaw = mc.player.getYaw();
        if (mc.options.sneakKey.isPressed()) {
            dFloatValue = -this.c.c().floatValue();
        } else {
            dFloatValue = mc.options.jumpKey.isPressed() ? this.c.c().floatValue() : 0.0d;
        }
        double motionY = dFloatValue;
        if (mc.player.input.movementForward == 0.0f && mc.player.input.movementSideways == 0.0f) {
            mc.player.setVelocity(0.0d, motionY, 0.0d);
            return;
        }
        if (mc.player.input.movementForward != 0.0f) {
            if (mc.player.input.movementSideways != 0.0f) {
                fSignum = (mc.player.input.movementForward > 0.0f ? -45.0f : 45.0f) * Math.signum(mc.player.input.movementSideways);
            } else {
                fSignum = 0.0f;
            }
            yaw += fSignum;
            mc.player.input.movementSideways = 0.0f;
            mc.player.input.movementForward = Math.signum(mc.player.input.movementForward);
        }
        double radians = Math.toRadians(yaw + 90.0f);
        double speedXZ = this.b.c().floatValue();
        double motionX = (((double) mc.player.input.movementForward) * speedXZ * Math.cos(radians)) + (((double) mc.player.input.movementSideways) * speedXZ * Math.sin(radians));
        double motionZ = ((((double) mc.player.input.movementForward) * speedXZ) * Math.sin(radians)) - ((((double) mc.player.input.movementSideways) * speedXZ) * Math.cos(radians));
        mc.player.setVelocity(motionX, motionY, motionZ);
    }
}
