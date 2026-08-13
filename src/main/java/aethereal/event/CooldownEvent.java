package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;
import net.minecraft.item.Item;

public class CooldownEvent extends Event implements IEvent {
    private final Item a;
    private final int b;

    public CooldownEvent(Item item, int cooldown) {
        this.a = item;
        this.b = cooldown;
    }

    @Generated
    public Item b() {
        return this.a;
    }

    @Generated
    public int c() {
        return this.b;
    }
}
