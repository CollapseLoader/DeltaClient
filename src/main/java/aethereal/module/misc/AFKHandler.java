package aethereal.module.misc;

import aethereal.core.Delta;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.InputEvent;
import aethereal.event.TickEvent;
import aethereal.handler.BaseHandler;

import aethereal.util.MathUtil;
import aethereal.util.Rotation;

import java.util.concurrent.ThreadLocalRandom;


public class AFKHandler extends BaseHandler implements Interface {
    private int b = -1;

    public int b() {
        return this.b;
    }

    public void a(int ticks) {
        this.b = ticks;
    }

    public boolean a() {
        return this.b > 0;
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.b > 0) {
            this.b--;
        }
    }

    @EventTarget
    public void a(InputEvent e) {
        if (this.b > 0) {
            e.setForward(ThreadLocalRandom.current().nextBoolean() ? 1.0f : -1.0f);
            e.setStrafe(ThreadLocalRandom.current().nextBoolean() ? 1.0f : -1.0f);
            Delta.getInstance().getModuleProcessor().k().startAiming(new Rotation(mc.player.getYaw() + MathUtil.a(-2.0f, 2.0f), MathUtil.b(mc.player.getPitch() + MathUtil.a(-1.0f, 1.0f), -90.0f, 90.0f)), 150.0f, 10, 1);
        }
    }
}
