package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;
import net.minecraft.entity.Entity;

public class AttackEvent extends Event implements IEvent {
    private final Entity a;

    public AttackEvent(Entity entity) {
        this.a = entity;
    }

    @Generated
    public Entity b() {
        return this.a;
    }
}
