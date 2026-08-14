package aethereal.event;

import aethereal.core.Event;


import net.minecraft.util.hit.HitResult;

public class CrosshairTargetEvent extends Event {
    private final float tickDelta;
    private HitResult target;

    public CrosshairTargetEvent(float tickDelta) {
        this.tickDelta = tickDelta;
    }

    public void setTarget(HitResult target) {
        this.target = target;
    }

    public float b() {
        return this.tickDelta;
    }

    public HitResult c() {
        return this.target;
    }
}
