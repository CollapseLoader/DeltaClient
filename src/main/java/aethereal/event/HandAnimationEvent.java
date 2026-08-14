package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;

public class HandAnimationEvent extends Event implements IEvent {
    private final MatrixStack a;
    private final Hand b;
    private final float c;
    private final int d;

    public HandAnimationEvent(MatrixStack matrices, Hand hand, float swingProgress, int armX) {
        this.a = matrices;
        this.b = hand;
        this.c = swingProgress;
        this.d = armX;
    }

    public MatrixStack b() {
        return this.a;
    }

    public Hand c() {
        return this.b;
    }

    public float d() {
        return this.c;
    }

    public int e() {
        return this.d;
    }
}
