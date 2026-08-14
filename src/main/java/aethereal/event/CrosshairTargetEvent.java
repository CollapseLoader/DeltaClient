package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import net.minecraft.util.hit.HitResult;

public class CrosshairTargetEvent extends Event implements IEvent {
    private final float a;
    private HitResult b;

    public CrosshairTargetEvent(float tickDelta) {
        this.a = tickDelta;
    }

    public void a(HitResult target) {
        this.b = target;
    }

    public float b() {
        return this.a;
    }

    public HitResult c() {
        return this.b;
    }
}
