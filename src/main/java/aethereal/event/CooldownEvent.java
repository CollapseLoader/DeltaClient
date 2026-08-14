package aethereal.event;

import aethereal.core.Event;


import net.minecraft.item.Item;

public class CooldownEvent extends Event {
    private final Item a;
    private final int b;

    public CooldownEvent(Item item, int cooldown) {
        this.a = item;
        this.b = cooldown;
    }

    public Item b() {
        return this.a;
    }

    public int c() {
        return this.b;
    }
}
