package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import net.minecraft.util.math.Vec3d;

public class CameraPositionEvent extends Event implements IEvent {
    private Vec3d a;

    public CameraPositionEvent(Vec3d position) {
        this.a = position;
    }

    public void a(Vec3d position) {
        this.a = position;
    }

    public Vec3d b() {
        return this.a;
    }
}
