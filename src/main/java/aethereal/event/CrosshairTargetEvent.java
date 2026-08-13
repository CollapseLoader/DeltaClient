package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;
import net.minecraft.util.hit.HitResult;

public class CrosshairTargetEvent extends Event implements IEvent {
    private final float a;
    private HitResult b;

    public CrosshairTargetEvent(float tickDelta) {
        this.a = tickDelta;
    }

    @Generated
    public void a(HitResult target) {
        this.b = target;
    }

    @Generated
    public float b() {
        return this.a;
    }

    @Generated
    public HitResult c() {
        return this.b;
    }
}
