package aethereal.util;

import aethereal.core.Interface;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import platform.inject.accessors.ClientPlayerEntityAccessor;

import java.util.Objects;

public class Rotation implements Interface {
    private float b;
    private float c;

    public Rotation() {
    }

    public Rotation(float yaw, float pitch) {
        this.b = yaw;
        this.c = pitch;
    }

    public Rotation(Entity entity) {
        this.b = entity.getYaw();
        this.c = entity.getPitch();
    }

    public static Rotation a() {
        if (mc.player == null) {
            return new Rotation(Look.b(), Look.c());
        }
        float py = mc.player.getYaw();
        float fy = Look.b();
        return new Rotation(py + MathHelper.wrapDegrees(fy - py), Look.c());
    }

    public static Rotation a(Vec3d eye, Vec3d point) {
        Vec3d diff = point.subtract(eye);
        double dist = Math.sqrt((diff.x * diff.x) + (diff.z * diff.z));
        float yaw = ((float) Math.toDegrees(Math.atan2(diff.z, diff.x))) - 90.0f;
        float pitch = (float) (-Math.toDegrees(Math.atan2(diff.y, dist)));
        return new Rotation(MathHelper.wrapDegrees(yaw), MathHelper.clamp(pitch, -90.0f, 90.0f));
    }

    public static Rotation b() {
        ClientPlayerEntityAccessor accessor = (ClientPlayerEntityAccessor) mc.player;
        return new Rotation(Objects.requireNonNull(accessor).getLastYaw(), accessor.getLastPitch());
    }

    public void a(float yaw) {
        this.b = yaw;
    }

    public void b(float pitch) {
        this.c = pitch;
    }

    public float c() {
        return this.b;
    }

    public float d() {
        return this.c;
    }

    public double a(Rotation targetRotation) {
        if (targetRotation == null) {
            return 0.0d;
        }
        double yawDelta = MathHelper.wrapDegrees(targetRotation.c() - this.b);
        double pitchDelta = MathHelper.wrapDegrees(targetRotation.d() - this.c);
        return Math.hypot(Math.abs(yawDelta), Math.abs(pitchDelta));
    }
}
