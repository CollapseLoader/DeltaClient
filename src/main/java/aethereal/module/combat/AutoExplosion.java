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

@ModuleRegister(a = "Auto Explosion", b = "Размещает кристалл на обсидиане и мгновенно его подрывает", c = Category.Combat)
public class AutoExplosion extends Module {
    private final BooleanSetting b = new BooleanSetting("Установить двойной кристалл", false);
    private BlockPos c;
    private int e;
    private int d = -1;

    public AutoExplosion() {
        a(this.b);
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.b()) {
            PlayerInteractBlockC2SPacket packet = (PlayerInteractBlockC2SPacket) event.d();
            if (packet instanceof PlayerInteractBlockC2SPacket) {
                if (aM_.player.getMainHandStack().getItem() == Items.OBSIDIAN) {
                    BlockHitResult hit = packet.getBlockHitResult();
                    this.c = hit.getBlockPos().offset(hit.getSide());
                    this.d = -1;
                    this.e = 0;
                }
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.c == null) {
            return;
        }
        int slot = InventoryUtil.a(Items.END_CRYSTAL, true);
        if (slot == -1 || !aM_.world.getBlockState(this.c).isOf(Blocks.OBSIDIAN) || !aM_.player.canInteractWithBlockAt(this.c, 0.0d)) {
            q();
            return;
        }
        if (this.e == 1 || (this.b.c().booleanValue() && this.e == 5)) {
            b(slot);
        }
        for (EndCrystalEntity crystal : aM_.world.getEntitiesByClass(EndCrystalEntity.class, new Box(this.c.up()).expand(0.5d), c -> {
            return true;
        })) {
            a(a(crystal.getBoundingBox()));
            if (this.e >= 3 && aM_.player.canInteractWithEntity(crystal, 0.0d)) {
                aM_.interactionManager.attackEntity(aM_.player, crystal);
                aM_.player.swingHand(Hand.MAIN_HAND);
                if (this.b.c().booleanValue() && this.e < 7) {
                    break;
                }
                q();
                return;
            }
        }
        int i = this.e + 1;
        this.e = i;
        if (i > (this.b.c().booleanValue() ? 8 : 4)) {
            q();
        }
    }

    private void b(int slot) {
        if (aM_.player.getInventory().selectedSlot != slot) {
            this.d = aM_.player.getInventory().selectedSlot;
            aM_.player.getInventory().selectedSlot = slot;
        }
        Vec3d center = this.c.toCenterPos();
        Vec3d hit = new Box(this.c).raycast(aM_.player.getEyePos(), center).orElse(center);
        a(center);
        aM_.interactionManager.interactBlock(aM_.player, Hand.MAIN_HAND, new BlockHitResult(hit, Direction.getFacing(hit.subtract(center)), this.c, false));
        aM_.player.swingHand(Hand.MAIN_HAND);
    }

    private Vec3d a(Box box) {
        Vec3d eye = aM_.player.getEyePos();
        return new Vec3d(MathHelper.clamp(eye.getX(), box.minX, box.maxX) + ((double) MathUtil.a(-0.1f, 0.1f)), MathHelper.clamp(eye.getY(), box.minY, box.maxY) + ((double) MathUtil.a(-0.1f, 0.1f)), MathHelper.clamp(eye.z, box.minZ, box.maxZ) + ((double) MathUtil.a(-0.1f, 0.1f)));
    }

    private void a(Vec3d point) {
        Rotation r = Rotation.a(aM_.player.getEyePos(), point);
        Delta.h().d().k().a(new Rotation(r.c() + MathUtil.a(-3.0f, 3.0f), r.d() + MathUtil.a(-3.0f, 3.0f)), 120.0f, 1, 2);
    }

    private void q() {
        if (this.d != -1) {
            aM_.player.getInventory().selectedSlot = this.d;
        }
        this.d = -1;
        this.c = null;
        this.e = 0;
    }

    @Override
    public void c() {
        super.c();
        q();
    }
}
