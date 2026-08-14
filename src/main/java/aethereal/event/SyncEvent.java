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

    public void a(ItemStack stack) {
        this.stack = stack;
    }

    public int b() {
        return this.slot;
    }

    public ItemStack c() {
        return this.stack;
    }
}
