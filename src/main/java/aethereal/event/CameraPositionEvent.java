package aethereal.event;

import aethereal.core.Event;
import aethereal.core.IEvent;

import lombok.Generated;
import net.minecraft.util.math.Vec3d;

public class CameraPositionEvent extends Event implements IEvent {
    private Vec3d a;

    public CameraPositionEvent(Vec3d position) {
        this.a = position;
    }

    @Generated
    public void a(Vec3d position) {
        this.a = position;
    }

    @Generated
    public Vec3d b() {
        return this.a;
    }
}
