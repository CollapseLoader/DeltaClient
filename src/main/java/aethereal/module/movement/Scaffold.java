package aethereal.module.movement;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.HotbarEvent;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.handler.UseableHandler;
import aethereal.setting.BooleanSetting;
import aethereal.util.Look;
import aethereal.util.MoveUtil;
import aethereal.util.Rotation;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;

import java.util.*;

@ModuleRegister(name = "Scaffold", description = "Автоматически ставит блоки под вами", category = Category.Movement)
public class Scaffold extends Module {
    private final BooleanSetting b = new BooleanSetting("Избегать падения", false);
    private final int[] e = {-1, -1, -1};
    private a c;
    private Vec3d d = Vec3d.ZERO;

    public Scaffold() {
        a(this.b);
    }

    @EventTarget
    public void a(InputEvent e) {
        if (r()) {
            return;
        }
        if (s() != null) {
            MoveUtil.a(e, Look.b(), 1);
            if (this.b.c().booleanValue()) {
                Delta.getInstance().getModuleProcessor().t().ah().b(e);
            }
        }
        if (a(mc.player.getMainHandStack()) || a(mc.player.getOffHandStack())) {
            return;
        }
        int hotbarSlot = d(true);
        if (hotbarSlot != -1) {
            if (mc.player.getInventory().selectedSlot != hotbarSlot && this.e[2] < 7 && Delta.getInstance().getModuleProcessor().v().getInventoryHandler().a().isEmpty()) {
                mc.player.getInventory().selectedSlot = hotbarSlot;
                this.e[2] = 9;
                return;
            }
            return;
        }
        int invSlot = d(false);
        if (this.e[2] < 5 && invSlot != -1 && Delta.getInstance().getModuleProcessor().v().getInventoryHandler().a().isEmpty()) {
            if (this.e[1] == -1) {
                this.e[1] = invSlot;
            }
            if (mc.player.getInventory().selectedSlot != 5) {
                mc.player.getInventory().selectedSlot = 5;
            }
            Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(invSlot, 5, 1);
            this.e[2] = 9;
        }
    }

    @EventTarget
    public void a(HotbarEvent event) {
        if (this.e[2] <= 5 || d(false) == -1) {
            return;
        }
        event.a(true);
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (!event.isSend() || !(event.getPacket() instanceof UpdateSelectedSlotC2SPacket)) {
            return;
        }
        this.e[2] = 9;
    }

    @EventTarget
    public void a(TickEvent e) {
        int[] iArr = this.e;
        iArr[2] = iArr[2] - 1;
    }

    @EventTarget
    public void a(GlobalEvent e) throws MatchException {
        if (s() == null || r()) {
            return;
        }
        if (this.c == null || !a(this.c)) {
            this.c = null;
            for (BlockPos target : q()) {
                this.c = b(target);
                if (this.c != null) {
                    break;
                }
            }
        }
        if (this.c == null) {
            return;
        }
        float t = mc.player.age + mc.getRenderTickCounter().getTickDelta(false);
        float smoothYaw = ((float) ((Math.sin(((double) t) * 0.40000001611738834d) * 3.0d) + (Math.sin((((double) t) * 0.950000126718632d) + 1.4000003101900576d) * 2.0d))) / 10.0f;
        float smoothPitch = ((float) ((Math.cos((((double) t) * 0.5d) + 0.7000001047992626d) * 0.5d) + (Math.cos((((double) t) * 0.7800000028540086d) + 3.099999110838922d) * 1.5d))) / 4.0f;
        Rotation rotation = b(this.c);
        rotation.a(rotation.c() + smoothYaw);
        rotation.b(rotation.d() + smoothPitch);
        Delta.getInstance().getModuleProcessor().k().startAiming(rotation, 100.0f, 7, 1);
        if (u() && !v() && !Delta.getInstance().getModuleProcessor().v().getStopHandler().a()) {
            ((platform.inject.invokers.MinecraftClientInvoker) mc).invokeDoItemUse();
            this.e[2] = 9;
            this.c = null;
        }
    }

    private boolean a(a data) {
        BlockPos placePos = data.a.offset(data.b);
        return a(mc.world.getBlockState(placePos)) && a(data.a) && !a(data.a, data.b).isEmpty();
    }

    private List<Vec3d> a(BlockPos pos, Direction face) throws MatchException {
        List<Vec3d> points = new ArrayList<>();
        Vec3d eye = t();
        double reach = mc.player.getBlockInteractionRange();
        Vec3d normal = new Vec3d(face.getOffsetX(), face.getOffsetY(), face.getOffsetZ());
        double[] offsets = {0.0d, -0.20000000236855192d, 0.200000000060146d, -0.3500000030268554d, 0.3500000598673184d, -0.4500000079401218d, 0.4499998886196472d};
        for (double u : offsets) {
            for (double v : offsets) {
                Vec3d point = a(pos, face, u, v);
                if (eye.distanceTo(point) <= reach && eye.subtract(point).normalize().dotProduct(normal) > 0.1000000076546522d) {
                    BlockHitResult hit = mc.world.raycast(new RaycastContext(eye, point, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
                    if (hit.getType() == HitResult.Type.MISS) {
                        points.add(point);
                    } else if (hit.getBlockPos().equals(pos)) {
                        points.add(point);
                    }
                }
            }
        }
        return points;
    }

    private List<BlockPos> q() {
        List<BlockPos> list = new ArrayList<>();
        this.d = this.d.multiply(0.6000003608875443d).add(new Vec3d(mc.player.getX() - mc.player.prevX, 0.0d, mc.player.getZ() - mc.player.prevZ).multiply(0.200000000060146d));
        for (int i = 0; i <= 2; i++) {
            Vec3d at = mc.player.getPos().add(this.d.multiply(i));
            BlockPos pos = BlockPos.ofFloored(at.x, mc.player.getY() - 1.0d, at.z);
            if (!list.contains(pos) && a(mc.world.getBlockState(pos))) {
                list.add(pos);
            }
        }
        list.sort(Comparator.comparingDouble(pos2 -> {
            return pos2.getSquaredDistance(mc.player.getPos());
        }));
        return list;
    }

    private boolean r() {
        List<UseableHandler.UseableTask> tasks = Delta.getInstance().getModuleProcessor().v().getUseableHandler().a();
        return !tasks.isEmpty() && tasks.getFirst().d() <= 1;
    }

    @Override
    public void b() {
        super.b();
        this.c = null;
        this.d = Vec3d.ZERO;
        if (mc.player != null) {
            this.e[0] = mc.player.getInventory().selectedSlot;
        }
    }

    @Override
    public void c() {
        super.c();
        if (this.e[0] != -1 && mc.player != null) {
            mc.player.getInventory().selectedSlot = this.e[0];
            if (this.e[1] != -1) {
                Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(this.e[1], 5, 1);
            }
        }
        this.e[0] = -1;
        this.e[1] = -1;
        this.e[2] = -1;
        this.c = null;
        this.d = Vec3d.ZERO;
    }

    private int d(boolean hotbarOnly) {
        int end = hotbarOnly ? 9 : 36;
        for (int i = 0; i < end; i++) {
            if (a(mc.player.getInventory().getStack(i))) {
                return i;
            }
        }
        return -1;
    }

    private boolean a(ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof BlockItem item) {
                return item.getBlock().getDefaultState().isOpaqueFullCube();
            }
        }
        return false;
    }

    private Hand s() {
        if (a(mc.player.getMainHandStack())) {
            return Hand.MAIN_HAND;
        }
        if (a(mc.player.getOffHandStack())) {
            return Hand.OFF_HAND;
        }
        return null;
    }

    private boolean a(BlockState state) {
        if (state.isReplaceable()) {
            return true;
        }
        return state.getBlock() == Blocks.SNOW && state.get(SnowBlock.LAYERS).intValue() < 8;
    }

    private boolean a(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        return !state.isAir() && state.getBlock() != Blocks.SNOW && !state.isReplaceable() && !state.getCollisionShape(mc.world, pos).isEmpty();
    }

    private a b(BlockPos pos) throws MatchException {
        a data = c(pos);
        if (data != null) {
            return data;
        }
        int[][] offsets = {new int[]{-1, 0, 0}, new int[]{1, 0, 0}, new int[]{0, 0, -1}, new int[]{0, 0, 1}, new int[]{-1, 0, -1}, new int[]{1, 0, 1}, new int[]{-1, 0, 1}, new int[]{1, 0, -1}, new int[]{0, -1, 0}, new int[]{-1, -1, 0}, new int[]{1, -1, 0}, new int[]{0, -1, -1}, new int[]{0, -1, 1}};
        Vec3d feet = mc.player.getPos();
        return Arrays.stream(offsets).map(o -> {
            return pos.add(o[0], o[1], o[2]);
        }).sorted(Comparator.comparingDouble(p -> {
            return p.getSquaredDistance(feet);
        })).map(this::c).filter((v0) -> {
            return Objects.nonNull(v0);
        }).findFirst().orElse(null);
    }

    private a c(BlockPos pos) throws MatchException {
        if (!a(mc.world.getBlockState(pos))) {
            return null;
        }
        a best = null;
        int bestCount = 0;
        for (Direction face : Direction.values()) {
            BlockPos neighbor = pos.offset(face);
            if (a(neighbor)) {
                Direction placeFace = face.getOpposite();
                List<Vec3d> points = a(neighbor, placeFace);
                if (points.size() > bestCount) {
                    bestCount = points.size();
                    best = new a(neighbor, placeFace, new BlockHitResult(a(neighbor, placeFace, 0.0d, 0.0d), placeFace, neighbor, false));
                }
            }
        }
        return best;
    }

    private Rotation b(a data) throws MatchException {
        Vec3d eye = t();
        List<Vec3d> points = a(data.a, data.b);
        if (points.isEmpty()) {
            return Rotation.a(eye, data.c.getPos());
        }
        Vec3d center = points.stream().reduce(Vec3d.ZERO, (v0, v1) -> {
            return v0.add(v1);
        }).multiply(1.0d / ((double) points.size()));
        Vec3d best = points.stream().min(Comparator.comparingDouble(point -> {
            return point.squaredDistanceTo(center);
        })).orElse(center);
        return Rotation.a(eye, best);
    }

    private Vec3d t() {
        Vec3d eye = mc.player.getEyePos();
        double fall = mc.player.getVelocity().y;
        return fall < 0.0d ? eye.add(0.0d, fall * 0.5d, 0.0d) : eye;
    }

    private Box d(BlockPos pos) {
        VoxelShape shape = mc.world.getBlockState(pos).getCollisionShape(mc.world, pos);
        return shape.isEmpty() ? new Box(0.0d, 0.0d, 0.0d, 1.0d, 1.0d, 1.0d) : shape.getBoundingBox();
    }

    private Vec3d a(BlockPos pos, Direction face, double u, double v) throws MatchException {
        Box shape = d(pos);
        double cx = ((double) pos.getX()) + ((shape.minX + shape.maxX) / 2.0d);
        double cy = ((double) pos.getY()) + ((shape.minY + shape.maxY) / 2.0d);
        double cz = ((double) pos.getZ()) + ((shape.minZ + shape.maxZ) / 2.0d);
        double sx = shape.getLengthX();
        double sy = shape.getLengthY();
        double sz = shape.getLengthZ();
        switch (AnonymousClass1.a[face.ordinal()]) {
            case 1:
                return new Vec3d(cx + (u * sx), ((double) pos.getY()) + shape.maxY, cz + (v * sz));
            case 2:
                return new Vec3d(cx + (u * sx), ((double) pos.getY()) + shape.minY, cz + (v * sz));
            case 3:
                return new Vec3d(cx + (u * sx), cy + (v * sy), ((double) pos.getZ()) + shape.minZ);
            case 4:
                return new Vec3d(cx + (u * sx), cy + (v * sy), ((double) pos.getZ()) + shape.maxZ);
            case 5:
                return new Vec3d(((double) pos.getX()) + shape.minX, cy + (v * sy), cz + (u * sz));
            case 6:
                return new Vec3d(((double) pos.getX()) + shape.maxX, cy + (v * sy), cz + (u * sz));
            default:
                throw new MatchException(null, null);
        }
    }

    private boolean u() {
        if (!(mc.crosshairTarget instanceof BlockHitResult hit)) {
            return false;
        }
        if (hit.getType() != HitResult.Type.BLOCK) {
            return false;
        }
        BlockPos placePos = this.c.a.offset(this.c.b);
        if (hit.getBlockPos().equals(this.c.a) && hit.getSide() == this.c.b) {
            return true;
        }
        return hit.getBlockPos().equals(placePos) && a(mc.world.getBlockState(placePos));
    }

    private boolean v() {
        BlockPos placePos = this.c.a.offset(this.c.b);
        return !mc.world.getEntitiesByClass(Entity.class, new Box(placePos), entity -> {
            return !entity.isSpectator() && entity.isAlive();
        }).isEmpty();
    }

    static class AnonymousClass1 {
        static final int[] a = new int[Direction.values().length];

        static {
            try {
                a[Direction.UP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                a[Direction.DOWN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                a[Direction.NORTH.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                a[Direction.SOUTH.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                a[Direction.WEST.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                a[Direction.EAST.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    record a(BlockPos a, Direction b, BlockHitResult c) {
    }
}
