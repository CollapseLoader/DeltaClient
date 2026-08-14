package aethereal.module.combat;

import aethereal.core.Interface;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import platform.inject.accessors.LivingEntityGravityInvoker;
import platform.inject.invokers.EntityCollisionPredictionInvoker;
import platform.inject.invokers.EntityMovementInvoker;

import java.util.ArrayList;
import java.util.Optional;

public class MaceUtil implements Interface {
    private MaceUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static boolean a() {
        return mc.player != null && mc.player.getMainHandStack().isOf(Items.MACE);
    }

    public static Optional<Vec3d> a(ClientPlayerEntity player, World world) {
        if (world == null || player.isOnGround()) {
            return Optional.empty();
        }
        if (player.getAbilities().flying || player.isGliding() || player.hasVehicle() || player.isClimbing()) {
            return Optional.empty();
        }
        if (player.hasStatusEffect(StatusEffects.LEVITATION)) {
            return Optional.empty();
        }
        if (player.isTouchingWater() || player.isInLava()) {
            return Optional.empty();
        }
        LivingEntityGravityInvoker gravitySrc = (LivingEntityGravityInvoker) player;
        double gravity = gravitySrc.getGravityInvoker();
        Box baseBox = player.getBoundingBox();
        double ox = 0.0d;
        double oy = 0.0d;
        double oz = 0.0d;
        Vec3d velocity = player.getVelocity();
        ArrayList<VoxelShape> shapes = new ArrayList<>();
        for (int tick = 0; tick < 400; tick++) {
            double vx = velocity.x * 0.9799995613681012d;
            double vy = (velocity.y - gravity) * 0.9799995613681012d;
            double vz = velocity.z * 0.9799995613681012d;
            Vec3d step = new Vec3d(vx, vy, vz);
            Box simBox = baseBox.offset(ox, oy, oz);
            EntityCollisionPredictionInvoker.findCollisionsForMovement(player, world, shapes, simBox);
            Vec3d allowed = EntityCollisionPredictionInvoker.adjustMovementForCollisions(player, step, simBox, world, shapes);
            if (step.y < -9.999995680744685E-5d && allowed.y > step.y + 9.999995999686725E-5d) {
                return Optional.of(player.getPos().add(ox + allowed.x, oy + allowed.y, oz + allowed.z));
            }
            ox += allowed.x;
            oy += allowed.y;
            oz += allowed.z;
            if (a(world, baseBox.offset(ox, oy, oz))) {
                return Optional.of(player.getPos().add(ox, oy, oz));
            }
            if (Math.abs(allowed.x - step.x) > 9.999995999686725E-5d) {
                vx = 0.0d;
            }
            if (Math.abs(allowed.y - step.y) > 9.999995999686725E-5d) {
                vy = 0.0d;
            }
            if (Math.abs(allowed.z - step.z) > 9.999995999686725E-5d) {
                vz = 0.0d;
            }
            velocity = new Vec3d(vx, vy, vz);
            if (velocity.lengthSquared() < 1.0000000090069629E-12d && step.y >= -9.999995680744685E-5d) {
                break;
            }
        }
        return Optional.empty();
    }

    public static boolean b() {
        Vec3d allowed;
        if (mc.world == null) {
            return false;
        }
        ClientPlayerEntity player = mc.player;
        if (player == null) {
            return false;
        }
        LivingEntityGravityInvoker gravity = (LivingEntityGravityInvoker) player;
        EntityMovementInvoker collider = (EntityMovementInvoker) player;
        double offsetY = 0.0d;
        Vec3d velocity = player.getVelocity();
        for (int tick = 0; tick < 2; tick++) {
            double nextVy = (velocity.y - gravity.getGravityInvoker()) * 0.9799995613681012d;
            Vec3d step = new Vec3d(0.0d, nextVy, 0.0d);
            Box box = player.getBoundingBox().offset(0.0d, offsetY, 0.0d);
            if (tick == 0 && offsetY == 0.0d) {
                allowed = collider.getAdjustMovementForCollisions(step);
            } else {
                ArrayList<VoxelShape> shapes = new ArrayList<>();
                EntityCollisionPredictionInvoker.findCollisionsForMovement(player, mc.world, shapes, box);
                allowed = EntityCollisionPredictionInvoker.adjustMovementForCollisions(player, step, box, mc.world, shapes);
            }
            if (nextVy < 0.0d && b(mc.world, box.offset(allowed.x, allowed.y, allowed.z))) {
                return true;
            }
            offsetY += allowed.y;
            velocity = new Vec3d(velocity.x, allowed.y, velocity.z);
        }
        return false;
    }

    private static boolean a(World world, Box box) {
        for (BlockPos pos : BlockPos.iterate(BlockPos.ofFloored(box.minX, box.minY, box.minZ), BlockPos.ofFloored(box.maxX, box.maxY, box.maxZ))) {
            FluidState fluid = world.getBlockState(pos).getFluidState();
            if (!fluid.isEmpty() && (fluid.isIn(FluidTags.WATER) || fluid.isIn(FluidTags.LAVA))) {
                return true;
            }
        }
        return false;
    }

    private static boolean b(World world, Box box) {
        BlockPos min = BlockPos.ofFloored(box.minX, box.minY, box.minZ);
        BlockPos max = BlockPos.ofFloored(box.maxX, box.maxY, box.maxZ);
        for (BlockPos pos : BlockPos.iterate(min, max)) {
            BlockState state = world.getBlockState(pos);
            if (state.isOf(Blocks.COBWEB) || state.isOf(Blocks.SWEET_BERRY_BUSH)) {
                return true;
            }
            FluidState fluid = state.getFluidState();
            if (!fluid.isEmpty() && (fluid.isIn(FluidTags.WATER) || fluid.isIn(FluidTags.LAVA))) {
                return true;
            }
        }
        return false;
    }
}
