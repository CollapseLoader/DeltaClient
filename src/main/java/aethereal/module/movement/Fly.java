package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.SliderSetting;

@ModuleRegister(a = "Fly", b = "Позволяет свободно летать по миру", c = Category.Movement)
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
        float yaw = aM_.player.getYaw();
        if (aM_.options.sneakKey.isPressed()) {
            dFloatValue = -this.c.c().floatValue();
        } else {
            dFloatValue = aM_.options.jumpKey.isPressed() ? this.c.c().floatValue() : 0.0d;
        }
        double motionY = dFloatValue;
        if (aM_.player.input.movementForward == 0.0f && aM_.player.input.movementSideways == 0.0f) {
            aM_.player.setVelocity(0.0d, motionY, 0.0d);
            return;
        }
        if (aM_.player.input.movementForward != 0.0f) {
            if (aM_.player.input.movementSideways != 0.0f) {
                fSignum = (aM_.player.input.movementForward > 0.0f ? -45.0f : 45.0f) * Math.signum(aM_.player.input.movementSideways);
            } else {
                fSignum = 0.0f;
            }
            yaw += fSignum;
            aM_.player.input.movementSideways = 0.0f;
            aM_.player.input.movementForward = Math.signum(aM_.player.input.movementForward);
        }
        double radians = Math.toRadians(yaw + 90.0f);
        double speedXZ = this.b.c().floatValue();
        double motionX = (((double) aM_.player.input.movementForward) * speedXZ * Math.cos(radians)) + (((double) aM_.player.input.movementSideways) * speedXZ * Math.sin(radians));
        double motionZ = ((((double) aM_.player.input.movementForward) * speedXZ) * Math.sin(radians)) - ((((double) aM_.player.input.movementSideways) * speedXZ) * Math.cos(radians));
        aM_.player.setVelocity(motionX, motionY, motionZ);
    }
}
