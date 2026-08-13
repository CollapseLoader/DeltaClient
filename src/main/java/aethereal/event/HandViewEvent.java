package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;
import lombok.Generated;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class HandViewEvent extends Event implements IEvent {
    private final MatrixStack a;
    private final ItemStack b;
    private final Hand c;

    @Generated
    public HandViewEvent(MatrixStack matrices, ItemStack stack, Hand hand) {
        this.a = matrices;
        this.b = stack;
        this.c = hand;
    }

    @Generated
    public MatrixStack b() {
        return this.a;
    }

    @Generated
    public ItemStack c() {
        return this.b;
    }

    @Generated
    public Hand d() {
        return this.c;
    }
}
