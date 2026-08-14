package aethereal.event;

import aethereal.core.Event;


import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

public class BoundingBoxEvent extends Event {
    public Box box;
    public Entity entity;

    public BoundingBoxEvent(Box box, Entity entity) {
        this.box = box;
        this.entity = entity;
    }

    public Box getBox() {
        return this.box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
