package aethereal.event;

import aethereal.core.Event;


import net.minecraft.item.ItemStack;

public class SyncEvent extends Event {
    private final int slot;
    private ItemStack stack;

    public SyncEvent(int slot, ItemStack stack) {
        this.slot = slot;
        this.stack = stack;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }
}
