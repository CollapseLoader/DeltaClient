package aethereal.event;

import aethereal.core.Event;


import net.minecraft.entity.Entity;

public class AttackEvent extends Event {
    private final Entity entity;

    public AttackEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity b() {
        return this.entity;
    }
}
