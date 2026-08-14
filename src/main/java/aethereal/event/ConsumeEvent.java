package aethereal.event;

import aethereal.core.Event;


import net.minecraft.item.ItemStack;

public class ConsumeEvent extends Event {
    private final ItemStack stack;

    public ConsumeEvent(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack b() {
        return this.stack;
    }
}
