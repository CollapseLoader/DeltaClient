package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class BoundingBoxEvent extends Event implements IEvent {
    public Box a;
    public Entity b;

    public BoundingBoxEvent(Box box, Entity entity) {
        this.a = box;
        this.b = entity;
    }

    public void a(Box box) {
        this.a = box;
    }

    public void a(Entity entity) {
        this.b = entity;
    }

    public Box b() {
        return this.a;
    }

    public Entity c() {
        return this.b;
    }
}
