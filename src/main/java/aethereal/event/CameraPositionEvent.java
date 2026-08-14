package aethereal.event;

import aethereal.core.Event;


import net.minecraft.util.math.Vec3d;

public class CameraPositionEvent extends Event {
    private Vec3d position;

    public CameraPositionEvent(Vec3d position) {
        this.position = position;
    }

    public void setPosition(Vec3d position) {
        this.position = position;
    }

    public Vec3d b() {
        return this.position;
    }
}
