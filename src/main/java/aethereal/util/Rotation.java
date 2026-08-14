package aethereal.util;

import aethereal.core.Interface;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import platform.inject.accessors.ClientPlayerEntityAccessor;

import java.util.Objects;

public class Rotation implements Interface {
    private float yaw;
    private float pitch;

    public Rotation() {
    }

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Rotation(Entity entity) {
        this.yaw = entity.getYaw();
        this.pitch = entity.getPitch();
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
        this.yaw = yaw;
    }

    public void b(float pitch) {
        this.pitch = pitch;
    }

    public float c() {
        return this.yaw;
    }

    public float d() {
        return this.pitch;
    }

    public double a(Rotation targetRotation) {
        if (targetRotation == null) {
            return 0.0d;
        }
        double yawDelta = MathHelper.wrapDegrees(targetRotation.c() - this.yaw);
        double pitchDelta = MathHelper.wrapDegrees(targetRotation.d() - this.pitch);
        return Math.hypot(Math.abs(yawDelta), Math.abs(pitchDelta));
    }
}
