package aethereal.event;

import aethereal.core.Event;


import net.minecraft.item.Item;

public class CooldownEvent extends Event {
    private final Item item;
    private final int cooldown;

    public CooldownEvent(Item item, int cooldown) {
        this.item = item;
        this.cooldown = cooldown;
    }

    public Item getItem() {
        return this.item;
    }

    public int c() {
        return this.cooldown;
    }
}
