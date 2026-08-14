package aethereal.event;

import aethereal.core.Event;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;

public class HandAnimationEvent extends Event {
    private final MatrixStack matrices;
    private final Hand hand;
    private final float swingProgress;
    private final int armX;

    public HandAnimationEvent(MatrixStack matrices, Hand hand, float swingProgress, int armX) {
        this.matrices = matrices;
        this.hand = hand;
        this.swingProgress = swingProgress;
        this.armX = armX;
    }

    public MatrixStack getMatrixStack() {
        return this.matrices;
    }

    public Hand getHand() {
        return this.hand;
    }

    public float getSwingProgress() {
        return this.swingProgress;
    }

    public int getArmX() {
        return this.armX;
    }
}
