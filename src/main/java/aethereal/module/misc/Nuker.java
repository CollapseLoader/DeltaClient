package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.DrawEvent;
import aethereal.event.InputEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.SliderSetting;
import aethereal.util.*;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;

@ModuleRegister(name = "Nuker", description = "Автоматически разрушает блоки в радиусе досягаемости", category = Category.Misc)
public class Nuker extends Module {
    private final ModeSetting b = new ModeSetting("Режим копания территории", "Шахта ФанТайм", "Шахта ФанТайм", "Общий");
    private final SliderSetting c = new SliderSetting("Дистанция копания", 4.0f, 1.0f, 6.0f, 0.5f);
    private final SliderSetting d = new SliderSetting("Скорость копания", 1.0f, 1.0f, 5.0f, 1.0f);
    private final BooleanSetting e = new BooleanSetting("Не копать под себя", true);
    private BlockPos targetPos;

    public Nuker() {
        a(this.b, this.c, this.d, this.e);
    }

    @Override
    public void b() {
        super.b();
        this.targetPos = null;
    }

    @Override
    public void c() {
        super.c();
        this.targetPos = null;
    }

    @EventTarget
    public void onTick(TickEvent event) {
        MineAssistant assistant = Delta.getInstance().getModuleProcessor().t().ak();
        this.targetPos = null;
        ItemStack tool = mc.player.getMainHandStack();
        if (tool.isDamageable() && tool.getMaxDamage() - tool.getDamage() < 50) {
            ChatUtil.sendMessage("Работа прекращена во избежание поломки кирки.");
            a();
            return;
        }
        if (this.b.l("Шахта ФанТайм") && ServerUtil.a.a$()) {
            assistant.detectMineArea();
        }
        boolean pickaxe = tool.getItem() instanceof PickaxeItem;
        int minY = this.e.c().booleanValue() ? mc.player.getBlockY() + ((pickaxe && InventoryUtil.a(tool, "Бульдозер")) ? 1 : 0) : Integer.MIN_VALUE;
        float range = this.c.h().floatValue();
        Box scan = mc.player.getBoundingBox().expand(range + 1.0f);
        Vec3d eye = mc.player.getEyePos();
        Vec3d look = mc.player.getRotationVec(1.0f);
        double bestScore = 1.7976922776554427E308d;
        Direction face = null;
        for (BlockPos pos : BlockPos.iterate(BlockPos.ofFloored(scan.minX, scan.minY, scan.minZ), BlockPos.ofFloored(scan.maxX, scan.maxY, scan.maxZ))) {
            if (pos.getY() >= minY && a(pos, pickaxe, assistant.getMineArea())) {
                Vec3d center = pos.toCenterPos();
                Vec3d diff = center.subtract(eye);
                double along = diff.dotProduct(look);
                Vec3d hit = along > 0.0d ? new Box(pos).raycast(eye, center).orElse(null) : null;
                if (hit != null && eye.squaredDistanceTo(hit) <= range * range && a(eye, pos)) {
                    double score = diff.subtract(look.multiply(along)).lengthSquared();
                    if (score < bestScore) {
                        bestScore = score;
                        this.targetPos = pos.toImmutable();
                        face = Direction.getFacing(hit.subtract(center));
                    }
                }
            }
        }
        if (this.targetPos != null) {
            Rotation base = Rotation.a(eye, this.targetPos.toCenterPos());
            Delta.getInstance().getModuleProcessor().k().startAiming(new Rotation(MathHelper.wrapDegrees(base.c() + MathUtil.a(-3.0f, 3.0f)), MathHelper.clamp(base.d() + MathUtil.a(-3.0f, 3.0f), -90.0f, 90.0f)), 180.0f, 1, 1);
            if (Rotation.b().a(base) <= 20.0d) {
                for (int i = 0; i < this.d.h().intValue(); i++) {
                    mc.interactionManager.updateBlockBreakingProgress(this.targetPos, face);
                }
                mc.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    @EventTarget
    public void onInput(InputEvent event) {
        if (this.targetPos != null) {
            MoveUtil.a(event, Look.b(), 5);
        }
    }

    @EventTarget
    public void onDraw(DrawEvent event) {
        if (event.c() && this.targetPos != null) {
            event.getDraw3DProcessor().a(event.h(), new Box(this.targetPos), ColorUtil.convertToARGB(255, 0, 0, InterfaceC0020Opcode.aN), 2.0f);
        }
    }

    private boolean a(BlockPos pos, boolean pickaxe, Box mineBox) {
        BlockState state = mc.world.getBlockState(pos);
        if (state.isAir()) {
            return false;
        }
        return !this.b.l("Шахта ФанТайм") || (pickaxe && mineBox.contains(pos.toCenterPos()) && state.calcBlockBreakingDelta(mc.player, mc.world, pos) >= 1.0f);
    }

    private boolean a(Vec3d eye, BlockPos pos) {
        Box box = new Box(pos).contract(0.05000000385685581d);
        for (int i = -1; i < 8; i++) {
            Vec3d point = i < 0 ? box.getCenter() : new Vec3d((i & 1) == 0 ? box.minX : box.maxX, (i & 2) == 0 ? box.minY : box.maxY, (i & 4) == 0 ? box.minZ : box.maxZ);
            if (mc.world.raycast(new RaycastContext(eye, point, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player)).getBlockPos().equals(pos)) {
                return true;
            }
        }
        return false;
    }
}
