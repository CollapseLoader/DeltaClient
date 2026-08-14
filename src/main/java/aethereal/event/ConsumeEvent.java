package aethereal.event;

import aethereal.core.Event;


import net.minecraft.item.ItemStack;

public class ConsumeEvent extends Event {
    private final ItemStack a;

    public ConsumeEvent(ItemStack stack) {
        this.a = stack;
    }

    public ItemStack b() {
        return this.a;
    }
}
