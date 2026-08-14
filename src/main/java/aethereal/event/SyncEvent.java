package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import net.minecraft.item.ItemStack;

public class SyncEvent extends Event implements IEvent {
    private final int a;
    private ItemStack b;

    public SyncEvent(int slot, ItemStack stack) {
        this.a = slot;
        this.b = stack;
    }

    public void a(ItemStack stack) {
        this.b = stack;
    }

    public int b() {
        return this.a;
    }

    public ItemStack c() {
        return this.b;
    }
}
