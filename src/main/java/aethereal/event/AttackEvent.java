package aethereal.event;

import aethereal.core.Event;


import net.minecraft.entity.Entity;

public class AttackEvent extends Event {
    private final Entity a;

    public AttackEvent(Entity entity) {
        this.a = entity;
    }

    public Entity b() {
        return this.a;
    }
}
