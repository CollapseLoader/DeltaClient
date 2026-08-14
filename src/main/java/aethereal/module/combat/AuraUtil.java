package aethereal.module.combat;

import aethereal.core.Interface;
import aethereal.handler.RotationProcessor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class AuraUtil implements Interface {
    private AuraUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static double a(Vec3d eye, Entity entity) {
        Box box = entity.getBoundingBox();
        double cx = MathHelper.clamp(eye.x, box.minX, box.maxX);
        double cy = MathHelper.clamp(eye.y, box.minY, box.maxY);
        double cz = MathHelper.clamp(eye.z, box.minZ, box.maxZ);
        double dx = cx - eye.x;
        double dy = cy - eye.y;
        double dz = cz - eye.z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public static double a(Entity entity) {
        if (mc.player == null) {
            return Double.POSITIVE_INFINITY;
        }
        return a(mc.player.getEyePos(), entity);
    }

    public static boolean a(Entity entity, double maxReach) {
        return a(entity) <= maxReach * maxReach;
    }

    public static boolean a(LivingEntity entity, double distance) {
        Vec3d eye = mc.player.getEyePos();
        Box box = entity.getBoundingBox();
        double cx = MathHelper.clamp(eye.x, box.minX, box.maxX);
        double cy = MathHelper.clamp(eye.y, box.minY, box.maxY);
        double cz = MathHelper.clamp(eye.z, box.minZ, box.maxZ);
        Vec3d delta = new Vec3d(cx - eye.x, cy - eye.y, cz - eye.z);
        float yaw = (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90.0d);
        float pitch = (float) (-Math.toDegrees(Math.atan2(delta.y, Math.hypot(delta.x, delta.z))));
        return a(yaw, pitch, distance, entity, true);
    }

    public static boolean a(float yaw, float pitch, double distance, Entity entity, boolean throwalls) {
        if (mc.player == null || mc.world == null) {
            return false;
        }
        return a(mc.player.getEyePos(), yaw, pitch, distance, entity, throwalls);
    }

    public static boolean a(Vec3d rayOrigin, float yaw, float pitch, double distance, Entity entity, boolean throwalls) {
        if (mc.player == null || mc.world == null) {
            return false;
        }
        Vec3d dir = Vec3d.fromPolar(pitch, yaw).multiply(distance);
        Optional<Vec3d> opt = entity.getBoundingBox().contains(rayOrigin) ? Optional.of(rayOrigin) : entity.getBoundingBox().raycast(rayOrigin, rayOrigin.add(dir));
        return opt.filter(vec3d -> throwalls || mc.world.raycast(new RaycastContext(rayOrigin, vec3d, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player)).getType() == HitResult.Type.MISS).isPresent();
    }

    public static boolean a(Vec3d from, LivingEntity entity, double reach) {
        Box box = entity.getBoundingBox();
        double[] steps = {0.0d, 0.125d, 0.25d, 0.375d, 0.5d, 0.625d, 0.75d, 0.875d, 1.0d};
        int lastIndex = steps.length - 1;
        double reachSq = reach * reach;
        for (int i = 0; i <= lastIndex; i++) {
            for (int j = 0; j <= lastIndex; j++) {
                for (int k = 0; k <= lastIndex; k++) {
                    if (i <= 0 || i >= lastIndex || j <= 0 || j >= lastIndex || k <= 0 || k >= lastIndex) {
                        Vec3d point = new Vec3d(MathHelper.lerp(steps[i], box.minX, box.maxX), MathHelper.lerp(steps[j], box.minY, box.maxY), MathHelper.lerp(steps[k], box.minZ, box.maxZ));
                        double distSq = from.squaredDistanceTo(point);
                        if (distSq > reachSq) {
                        } else {
                            Vec3d end = point.add(from.subtract(point).multiply(0.05000000993895991d / Math.sqrt(distSq)));
                            if (mc.world.raycast(new RaycastContext(from, end, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, mc.player)).getType() == HitResult.Type.MISS) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean a(int ticks, LivingEntity target, boolean checks) {
        if (!checks && ticks >= 7 && a(target, 3.0d) && mc.player.getAttackCooldownProgress(0.5f) > 0.7f) {
            return a();
        }
        return false;
    }

    public static boolean a() {
        if (mc.player == null || mc.world == null) {
            return false;
        }
        double dy = (mc.player.getVelocity().y - 0.08000000049877275d) * 0.9799995837206814d;
        if (dy >= 0.0d) {
            return false;
        }
        Box moved = mc.player.getBoundingBox().offset(0.0d, dy, 0.0d);
        Box feet = new Box(moved.minX, moved.minY - 0.010000001417203743d, moved.minZ, moved.maxX, moved.minY, moved.maxZ);
        return mc.world.isBlockSpaceEmpty(mc.player, feet);
    }

    public static Vec3d a(Vec3d eye, LivingEntity target, double reach, boolean throughWalls) {
        Box bb = target.getBoundingBox();
        boolean mace = MaceUtil.a();
        Vec3d aimEye = (!mace || mc.player == null) ? eye : eye.add(mc.player.getVelocity());
        double mx = (bb.minX + bb.maxX) * 0.5d;
        double mz = (bb.minZ + bb.maxZ) * 0.5d;
        Vec3d targetEye = target.getPos().add(0.0d, target.getStandingEyeHeight(), 0.0d);
        double distToTargetEye = aimEye.distanceTo(targetEye);
        Vec3d aimOrigin = aimEye;
        if (mace && distToTargetEye > 3.0d) {
            aimOrigin = new Vec3d(aimEye.x, targetEye.y, aimEye.z);
        }
        double blendDist = mace ? Math.min(distToTargetEye, 3.0d) : distToTargetEye;
        double aimHeight = aimEye.y;
        if (mace && distToTargetEye > 3.0d) {
            aimHeight = targetEye.y;
        }
        double ay = MathHelper.lerp(MathHelper.clamp(blendDist / 3.0d, 0.0d, 1.0d), bb.minY, MathHelper.clamp(aimHeight, bb.minY, bb.maxY));
        Vec3d ideal = new Vec3d(mx, ay, mz);
        List<Vec3d> pts = new ArrayList<>();
        pts.add(ideal);
        double[] t = {0.0d, 0.125d, 0.25d, 0.375d, 0.5d, 0.625d, 0.75d, 0.875d, 1.0d};
        int last = t.length - 1;
        for (int a = 0; a < t.length; a++) {
            for (int b = 0; b < t.length; b++) {
                for (int c = 0; c < t.length; c++) {
                    if (a == 0 || a == last || b == 0 || b == last || c == 0 || c == last) {
                        pts.add(new Vec3d(MathHelper.lerp(t[a], bb.minX, bb.maxX), MathHelper.lerp(t[b], bb.minY, bb.maxY), MathHelper.lerp(t[c], bb.minZ, bb.maxZ)));
                    }
                }
            }
        }
        for (double pad : new double[]{0.0d, 0.20000001551382535d}) {
            List<Vec3d> visible = new ArrayList<>();
            for (Vec3d p : pts) {
                Vec3d delta = p.subtract(aimOrigin);
                double len = delta.length();
                double limit = reach + pad;
                if (mace || len <= limit) {
                    float traceDist = (float) (mace ? len + pad + 0.010000001417203743d : limit);
                    if (a(aimOrigin, (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(delta.z, delta.x)) - 90.0d), (float) (-Math.toDegrees(Math.atan2(delta.y, Math.hypot(delta.x, delta.z)))), traceDist, target, false)) {
                        visible.add(p);
                    }
                }
            }
            if (!visible.isEmpty()) {
                Vec3d centroid = visible.stream().reduce(Vec3d.ZERO, (v0, v1) -> {
                    return v0.add(v1);
                }).multiply(1.0d / ((double) visible.size()));
                return visible.stream().min(Comparator.comparingDouble(pt -> {
                    return pt.squaredDistanceTo(centroid);
                })).get().subtract(aimOrigin);
            }
            if (throughWalls) {
                List<Vec3d> through = new ArrayList<>();
                for (Vec3d p2 : pts) {
                    Vec3d delta2 = p2.subtract(aimOrigin);
                    double len2 = delta2.length();
                    double limit2 = reach + pad;
                    if (mace || len2 <= limit2) {
                        float traceDist2 = (float) (mace ? len2 + pad + 0.010000001417203743d : limit2);
                        if (a(aimOrigin, (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(delta2.z, delta2.x)) - 90.0d), (float) (-Math.toDegrees(Math.atan2(delta2.y, Math.hypot(delta2.x, delta2.z)))), traceDist2, target, true)) {
                            through.add(p2);
                        }
                    }
                }
                if (!through.isEmpty()) {
                    Vec3d centroid2 = through.stream().reduce(Vec3d.ZERO, (v0, v1) -> {
                        return v0.add(v1);
                    }).multiply(1.0d / ((double) through.size()));
                    return through.stream().min(Comparator.comparingDouble(pt2 -> {
                        return pt2.squaredDistanceTo(centroid2);
                    })).get().subtract(aimOrigin);
                }
            }
        }
        return Vec3d.ZERO;
    }

    public static boolean b() {
        if (mc.player == null || mc.player.getWorld() == null) {
            return false;
        }
        World world = mc.player.getWorld();
        BlockPos eye = BlockPos.ofFloored(mc.player.getEyePos());
        FluidState fluid = world.getFluidState(eye);
        return !mc.player.hasStatusEffect(StatusEffects.LEVITATION) && !mc.player.hasStatusEffect(StatusEffects.BLINDNESS) && !fluid.isIn(FluidTags.WATER) && !fluid.isIn(FluidTags.LAVA) && !mc.player.getAbilities().flying && !mc.player.isGliding() && !mc.player.isClimbing() && !mc.player.hasVehicle();
    }

    public static boolean c() {
        return mc.player != null && b() && mc.player.fallDistance > 0.0f && !mc.player.isOnGround();
    }

    public static float a(float start, float end, float amount) {
        float amountClamped = MathHelper.clamp(amount, 0.0f, 1.0f);
        float delta = MathHelper.wrapDegrees(end - start);
        if (Math.abs(delta) < 0.5f) {
            return end;
        }
        float stepped = MathHelper.wrapDegrees(start + (delta * amountClamped));
        float patched = RotationProcessor.snapToGCD(start, stepped);
        float remaining = MathHelper.wrapDegrees(end - patched);
        return Math.abs(remaining) < 0.5f ? end : patched;
    }
}
