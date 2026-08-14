package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import net.minecraft.item.ItemStack;

public class ConsumeEvent extends Event implements IEvent {
    private final ItemStack a;

    public ConsumeEvent(ItemStack stack) {
        this.a = stack;
    }

    public ItemStack b() {
        return this.a;
    }
}
