package aethereal.util;

import aethereal.core.EventManager;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.LookEvent;
import aethereal.event.RotationEvent;
import net.minecraft.util.math.MathHelper;

public class Look implements Interface {
    private static float freeYaw;
    private static float freePitch;
    private boolean active;

    public Look() {
        EventManager.a(this);
    }

    public static float b() {
        return freeYaw;
    }

    public static float c() {
        return freePitch;
    }

    public static void a(float newFreeYaw) {
        freeYaw = newFreeYaw;
    }

    public static void b(float newFreePitch) {
        freePitch = newFreePitch;
    }

    private static void d() {
        if (mc.player != null) {
            float py = mc.player.getYaw();
            float fy = freeYaw;
            mc.player.setYaw(py + MathHelper.wrapDegrees(fy - py));
            mc.player.setPitch(freePitch);
        }
    }

    public boolean a() {
        return this.active;
    }

    @EventTarget
    private void a(LookEvent e) {
        if (this.active) {
            a(e.yaw, e.pitch);
            e.a(true);
        }
    }

    @EventTarget
    private void a(RotationEvent e) {
        if (this.active) {
            e.setYaw(freeYaw);
            e.setPitch(freePitch);
        } else {
            freeYaw = e.getYaw();
            freePitch = e.getPitch();
        }
    }

    public void a(boolean state) {
        if (this.active != state) {
            this.active = state;
            d();
        }
    }

    private void a(double yaw, double pitch) {
        double d0 = pitch * 0.15000001238751678d;
        double d1 = yaw * 0.15000001238751678d;
        freePitch = (float) (((double) freePitch) + d0);
        freeYaw = (float) (((double) freeYaw) + d1);
        freePitch = MathHelper.clamp(freePitch, -90.0f, 90.0f);
    }
}
