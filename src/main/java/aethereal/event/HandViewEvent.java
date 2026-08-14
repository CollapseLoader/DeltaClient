package aethereal.event;

import aethereal.core.Event;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class HandViewEvent extends Event {
    private final MatrixStack a;
    private final ItemStack b;
    private final Hand c;

    public HandViewEvent(MatrixStack matrices, ItemStack stack, Hand hand) {
        this.a = matrices;
        this.b = stack;
        this.c = hand;
    }

    public MatrixStack b() {
        return this.a;
    }

    public ItemStack c() {
        return this.b;
    }

    public Hand d() {
        return this.c;
    }
}
