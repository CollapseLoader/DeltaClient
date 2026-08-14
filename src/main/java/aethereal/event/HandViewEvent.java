package aethereal.event;

import aethereal.core.Event;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class HandViewEvent extends Event {
    private final MatrixStack matrices;
    private final ItemStack stack;
    private final Hand hand;

    public HandViewEvent(MatrixStack matrices, ItemStack stack, Hand hand) {
        this.matrices = matrices;
        this.stack = stack;
        this.hand = hand;
    }

    public MatrixStack getMatrices() {
        return this.matrices;
    }

    public ItemStack c() {
        return this.stack;
    }

    public Hand d() {
        return this.hand;
    }
}
