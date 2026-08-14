package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.ClickEvent;
import aethereal.event.TickEvent;
import aethereal.event.WillLandEvent;
import aethereal.util.Look;
import aethereal.util.Rotation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TridentItem;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.StreamSupport;

@ModuleRegister(name = "Projectile Helper", description = "Помогает целиться по противнику при стрельбе из лука или трезубца", category = Category.Combat)
public class ProjectileHelper extends Module {
    private final LivingEntity[] b = new LivingEntity[2];
    private final Vec3d[] c = new Vec3d[5];
    private int d;
    private int e;
    private boolean g;
    private boolean h;
    private boolean f = true;

    public LivingEntity q() {
        return this.b[0];
    }

    public boolean r() {
        if (this.b[0] == null || mc.player == null || !mc.player.isUsingItem()) {
            return false;
        }
        ItemStack active = mc.player.getActiveItem();
        return active.getItem().getMaxUseTime(active, mc.player) - mc.player.getItemUseTimeLeft() > 2;
    }

    @Override
    public void c() {
        super.c();
        s();
    }

    @EventTarget
    public void a(WillLandEvent event) {
        this.g = event.b();
    }

    @EventTarget
    public void a(TickEvent event) {
        Rotation aim;
        if (mc.player == null || mc.world == null) {
            return;
        }
        ItemStack stack = mc.player.getStackInHand(Hand.MAIN_HAND);
        if (!(stack.getItem() instanceof BowItem) && !(stack.getItem() instanceof TridentItem)) {
            s();
            return;
        }
        if (!mc.player.isUsingItem()) {
            this.f = true;
        }
        if (!this.f) {
            s();
            return;
        }
        this.h = mc.player.input.playerInput.jump() && (mc.player.isOnGround() || this.g);
        a(t());
        if (this.b[0] != null) {
            Vec3d[] positions = this.c;
            int i = this.e;
            this.e = i + 1;
            positions[i % this.c.length] = new Vec3d(this.b[0].getX() - this.b[0].prevX, 0.0d, this.b[0].getZ() - this.b[0].prevZ);
        }
        if (r() && (aim = a(stack)) != null) {
            Delta.getInstance().getModuleProcessor().k().a(aim, 180.0f, 1, 1);
        }
    }

    @EventTarget
    public void a(ClickEvent event) {
        if (event.b() && event.h() == 0 && mc.player != null && mc.player.isUsingItem()) {
            this.f = !this.f;
        }
    }

    private void s() {
        LivingEntity[] targets = this.b;
        this.b[1] = null;
        targets[0] = null;
        this.d = 0;
        Arrays.fill(this.c, null);
    }

    private LivingEntity t() {
        Vec3d eye = mc.player.getEyePos();
        Vec3d look = Vec3d.fromPolar(Look.c(), Look.b());
        return (LivingEntity) StreamSupport.stream(mc.world.getEntities().spliterator(), false)
                .filter(PlayerEntity.class::isInstance)
                .map(e -> (PlayerEntity) e)
                .filter(e -> e != mc.player && e.isAlive() && !Delta.getInstance().getModuleProcessor().e().d(e.getName().getString()) && eye.squaredDistanceTo(e.getBoundingBox().getCenter()) <= 14400.0d)
                .min(Comparator.comparingDouble(e2 -> -look.dotProduct(e2.getBoundingBox().getCenter().subtract(eye).normalize()))).orElse(null);
    }

    private void a(LivingEntity best) {
        if (best != this.b[1]) {
            this.b[1] = best;
            this.d = 0;
        } else {
            this.d++;
        }
        if (this.b[0] != this.b[1]) {
            if (this.b[0] == null || this.d >= 4) {
                this.b[0] = this.b[1];
                Arrays.fill(this.c, null);
            }
        }
    }

    private Vec3d u() {
        Vec3d sum = Vec3d.ZERO;
        int count = 0;
        for (Vec3d entry : this.c) {
            if (entry != null && entry.horizontalLengthSquared() > 1.000000229429758E-6d) {
                sum = sum.add(entry);
                count++;
            }
        }
        return count == 0 ? Vec3d.ZERO : sum.multiply(1.0d / ((double) count));
    }

    private Rotation a(ItemStack stack) {
        Vec3d shooter = v();
        Vec3d origin = mc.player.getEyePos().add(0.0d, -0.1000000074661073d, 0.0d);
        double speed = stack.getItem() instanceof BowItem ? b(stack) : 2.5d;
        Box box = this.b[0].getBoundingBox();
        Vec3d motion = u();
        Vec3d aim = box.getCenter();
        float yaw = 0.0f;
        float pitch = 0.0f;
        for (int i = 0; i < 6; i++) {
            yaw = a(origin, aim);
            pitch = a(origin, aim, shooter, speed);
            double[] shot = a(origin, Vec3d.fromPolar(pitch, yaw).multiply(speed).add(shooter), Math.hypot(aim.x - origin.getX(), aim.z - origin.getZ()), true);
            if (shot == null) {
                return null;
            }
            Vec3d moved = box.getCenter().add(motion.multiply(Math.min(shot[1] + 6.0d, 13.0d)));
            if (moved.squaredDistanceTo(aim) < 9.999996044721066E-5d) {
                break;
            }
            aim = moved;
        }
        Rotation rotation = new Rotation(MathHelper.wrapDegrees(yaw), pitch);
        float time = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
        float smoothW = ((float) ((((Math.sin(time * 0.8f) * 11.0d) + (Math.sin((((double) time) * 0.04000001688754603d) + 17.200001527756587d) * 1.5d)) + (Math.sin((((double) time) * 0.11000000003049541d) + 5.800002923050999d) * 3.0d)) + (Math.sin((((double) time) * 0.07000000374109333d) + 12.300000031704212d)))) / 4.0f;
        float smoothH = ((float) (Math.sin(((double) time) * 0.1000000001867308d) + (Math.sin((((double) time) * 0.029999988014174556d) + 54.09998474500903d) * 0.5d))) / 2.0f;
        boolean tridentEarly = (stack.getItem() instanceof TridentItem) && stack.getItem().getMaxUseTime(stack, mc.player) - mc.player.getItemUseTimeLeft() < 9;
        if (!tridentEarly) {
            smoothW = MathHelper.clamp(smoothW, -0.3f, 0.3f);
            smoothH = MathHelper.clamp(smoothH, -0.3f, 0.3f);
        }
        rotation.a(rotation.c() + smoothW);
        rotation.b(rotation.d() + smoothH);
        return rotation;
    }

    private float a(Vec3d origin, Vec3d aim, Vec3d shooter, double speed) {
        float low = -90.0f;
        float high = 90.0f;
        float yaw = a(origin, aim);
        double target = Math.hypot(aim.x - origin.getX(), aim.z - origin.getZ());
        double height = aim.y - origin.getY();
        for (int i = 0; i < 24; i++) {
            float middle = (low + high) / 2.0f;
            double[] shot = a(origin, Vec3d.fromPolar(middle, yaw).multiply(speed).add(shooter), target, false);
            if (shot == null || shot[0] >= height) {
                low = middle;
            } else {
                high = middle;
            }
        }
        return (low + high) / 2.0f;
    }

    private double[] a(Vec3d origin, Vec3d velocity, double target, boolean blocked) {
        Vec3d position = origin;
        Vec3d current = velocity;
        double travelled = 0.0d;
        for (int tick = 1; tick <= 100; tick++) {
            Vec3d next = position.add(current);
            if (blocked && mc.world.raycast(new RaycastContext(position, next, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player)).getType() != HitResult.Type.MISS) {
                return null;
            }
            double reached = Math.hypot(next.x - origin.getX(), next.z - origin.getZ());
            if (reached >= target) {
                double alpha = reached == travelled ? 1.0d : (target - travelled) / (reached - travelled);
                return new double[]{MathHelper.lerp(alpha, position.y, next.y) - origin.getY(), ((double) (tick - 1)) + alpha};
            }
            position = next;
            travelled = reached;
            current = current.multiply(a(position) ? 0.6000002908794272d : 0.9900000228356232d).add(0.0d, -0.050000001868616015d, 0.0d);
        }
        return null;
    }

    private boolean a(Vec3d position) {
        return mc.world.getBlockState(BlockPos.ofFloored(position)).getFluidState().isIn(FluidTags.WATER);
    }

    private Vec3d v() {
        Vec3d velocity = new Vec3d(mc.player.getX() - mc.player.prevX, mc.player.getY() - mc.player.prevY, mc.player.getZ() - mc.player.prevZ);
        if (!this.h) {
            return new Vec3d(velocity.x, mc.player.isOnGround() ? 0.0d : velocity.y, velocity.z);
        }
        float yaw = mc.player.getYaw() * 0.017453292f;
        double sprint = mc.player.isSprinting() ? 0.19999997617511883d : 0.0d;
        return new Vec3d(velocity.x - (((double) MathHelper.sin(yaw)) * sprint), Math.max(0.42f + mc.player.getJumpBoostVelocityModifier(), velocity.y), velocity.z + (((double) MathHelper.cos(yaw)) * sprint));
    }

    private double b(ItemStack stack) {
        float pull = 1.0f;
        ItemStack active = mc.player.getActiveItem();
        if (mc.player.isUsingItem() && (active.getItem() instanceof BowItem)) {
            float progress = ((active.getItem().getMaxUseTime(active, mc.player) - mc.player.getItemUseTimeLeft()) + 1.5f) / 20.0f;
            pull = Math.min(((progress * progress) + (progress * 2.0f)) / 3.0f, 1.0f);
        }
        return ((double) pull) * 3.0d;
    }

    private float a(Vec3d from, Vec3d to) {
        return (float) Math.toDegrees(Math.atan2(-(to.getX() - from.getX()), to.getZ() - from.getZ()));
    }
}
