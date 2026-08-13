package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class BoundingBoxEvent extends Event implements IEvent {
    public Box a;
    public Entity b;

    @Generated
    public BoundingBoxEvent(Box box, Entity entity) {
        this.a = box;
        this.b = entity;
    }

    @Generated
    public void a(Box box) {
        this.a = box;
    }

    @Generated
    public void a(Entity entity) {
        this.b = entity;
    }

    @Generated
    public Box b() {
        return this.a;
    }

    @Generated
    public Entity c() {
        return this.b;
    }
}
