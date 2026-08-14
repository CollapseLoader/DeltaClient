package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import net.minecraft.block.Blocks;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;

@ModuleRegister(name = "Auto Explosion", description = "Размещает кристалл на обсидиане и мгновенно его подрывает", category = Category.Combat)
public class AutoExplosion extends Module {
    private final BooleanSetting b = new BooleanSetting("Установить двойной кристалл", false);
    private BlockPos targetPos;
    private int placeDelay;
    private int crystalState = -1;

    public AutoExplosion() {
        a(this.b);
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        if (event.isSend()) {
            PlayerInteractBlockC2SPacket packet = (PlayerInteractBlockC2SPacket) event.getPacket();
            if (packet instanceof PlayerInteractBlockC2SPacket) {
                if (mc.player.getMainHandStack().getItem() == Items.OBSIDIAN) {
                    BlockHitResult hit = packet.getBlockHitResult();
                    this.targetPos = hit.getBlockPos().offset(hit.getSide());
                    this.crystalState = -1;
                    this.placeDelay = 0;
                }
            }
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (this.targetPos == null) {
            return;
        }
        int slot = InventoryUtil.a(Items.END_CRYSTAL, true);
        if (slot == -1 || !mc.world.getBlockState(this.targetPos).isOf(Blocks.OBSIDIAN) || !mc.player.canInteractWithBlockAt(this.targetPos, 0.0d)) {
            resetState();
            return;
        }
        if (this.placeDelay == 1 || (this.b.c().booleanValue() && this.placeDelay == 5)) {
            placeCrystal(slot);
        }
        for (EndCrystalEntity crystal : mc.world.getEntitiesByClass(EndCrystalEntity.class, new Box(this.targetPos.up()).expand(0.5d), c -> {
            return true;
        })) {
            aimAtPoint(getAimPoint(crystal.getBoundingBox()));
            if (this.placeDelay >= 3 && mc.player.canInteractWithEntity(crystal, 0.0d)) {
                mc.interactionManager.attackEntity(mc.player, crystal);
                mc.player.swingHand(Hand.MAIN_HAND);
                if (this.b.c().booleanValue() && this.placeDelay < 7) {
                    break;
                }
                resetState();
                return;
            }
        }
        int i = this.placeDelay + 1;
        this.placeDelay = i;
        if (i > (this.b.c().booleanValue() ? 8 : 4)) {
            resetState();
        }
    }

    private void placeCrystal(int slot) {
        if (mc.player.getInventory().selectedSlot != slot) {
            this.crystalState = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = slot;
        }
        Vec3d center = this.targetPos.toCenterPos();
        Vec3d hit = new Box(this.targetPos).raycast(mc.player.getEyePos(), center).orElse(center);
        aimAtPoint(center);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, new BlockHitResult(hit, Direction.getFacing(hit.subtract(center)), this.targetPos, false));
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private Vec3d getAimPoint(Box box) {
        Vec3d eye = mc.player.getEyePos();
        return new Vec3d(MathHelper.clamp(eye.getX(), box.minX, box.maxX) + ((double) MathUtil.a(-0.1f, 0.1f)), MathHelper.clamp(eye.getY(), box.minY, box.maxY) + ((double) MathUtil.a(-0.1f, 0.1f)), MathHelper.clamp(eye.z, box.minZ, box.maxZ) + ((double) MathUtil.a(-0.1f, 0.1f)));
    }

    private void aimAtPoint(Vec3d point) {
        Rotation r = Rotation.a(mc.player.getEyePos(), point);
        Delta.getInstance().getModuleProcessor().k().startAiming(new Rotation(r.c() + MathUtil.a(-3.0f, 3.0f), r.d() + MathUtil.a(-3.0f, 3.0f)), 120.0f, 1, 2);
    }

    private void resetState() {
        if (this.crystalState != -1) {
            mc.player.getInventory().selectedSlot = this.crystalState;
        }
        this.crystalState = -1;
        this.targetPos = null;
        this.placeDelay = 0;
    }

    @Override
    public void c() {
        super.c();
        resetState();
    }
}
