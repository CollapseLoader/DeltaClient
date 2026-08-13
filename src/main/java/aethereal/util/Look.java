package aethereal.util;

import aethereal.core.EventManager;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.event.LookEvent;
import aethereal.event.RotationEvent;
import lombok.Generated;
import net.minecraft.util.math.MathHelper;

public class Look implements Interface {
    private static float c;
    private static float d;
    private boolean b;

    public Look() {
        EventManager.a(this);
    }

    @Generated
    public static float b() {
        return c;
    }

    @Generated
    public static float c() {
        return d;
    }

    @Generated
    public static void a(float freeYaw) {
        c = freeYaw;
    }

    @Generated
    public static void b(float freePitch) {
        d = freePitch;
    }

    private static void d() {
        if (aM_.player != null) {
            float py = aM_.player.getYaw();
            float fy = c;
            aM_.player.setYaw(py + MathHelper.wrapDegrees(fy - py));
            aM_.player.setPitch(d);
        }
    }

    @Generated
    public boolean a() {
        return this.b;
    }

    @EventTarget
    private void a(LookEvent e) {
        if (this.b) {
            a(e.a, e.b);
            e.a(true);
        }
    }

    @EventTarget
    private void a(RotationEvent e) {
        if (this.b) {
            e.a(c);
            e.b(d);
        } else {
            c = e.b();
            d = e.c();
        }
    }

    public void a(boolean state) {
        if (this.b != state) {
            this.b = state;
            d();
        }
    }

    private void a(double yaw, double pitch) {
        double d0 = pitch * 0.15000001238751678d;
        double d1 = yaw * 0.15000001238751678d;
        d = (float) (((double) d) + d0);
        c = (float) (((double) c) + d1);
        d = MathHelper.clamp(d, -90.0f, 90.0f);
    }
}
