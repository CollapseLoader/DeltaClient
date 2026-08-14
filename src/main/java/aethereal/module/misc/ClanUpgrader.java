package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.util.*;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

@ModuleRegister(name = "Clan Upgrader", description = "Быстро прокачивает клан с помощью редстоуна и факела", category = Category.Misc)
public class ClanUpgrader extends Module {
    private int upgradeCount;

    @Override
    public void c() {
        super.c();
        if (this.upgradeCount != -1) {
            mc.player.getInventory().selectedSlot = this.upgradeCount;
            this.upgradeCount = -1;
        }
    }

    @EventTarget
    public void onTick(TickEvent event) {
        int redstone = InventoryUtil.a(Items.REDSTONE, true);
        int torch = InventoryUtil.a(Items.TORCH, true);
        int target = redstone != -1 ? redstone : torch;
        if (redstone == -1 && torch == -1) {
            ChatUtil.sendMessage("Вам необходимо иметь факел или редстоун в хотбаре");
            a();
            return;
        }
        float randomPitch = ((float) (Math.sin(System.currentTimeMillis() / 1220.0d) * ((double) (Math.abs(90.0f - mc.player.getPitch()) / 8.0f)))) + MathUtil.a(-0.1f, 0.1f);
        Rotation rotation = new Rotation(Look.b() + MathUtil.a(-1.0f, 1.0f), MathUtil.b(88.0f + randomPitch, -90.0f, 90.0f));
        Delta.getInstance().getModuleProcessor().k().startAiming(rotation, 90.0f, 1, 1);
        if (this.upgradeCount == -1) {
            this.upgradeCount = mc.player.getInventory().selectedSlot;
        }
        if (mc.player.getInventory().selectedSlot != target) {
            mc.player.getInventory().selectedSlot = target;
        }
        if (Rotation.b().a(rotation) <= 1.0d) {
            BlockPos position = mc.player.getBlockPos();
            if (mc.world.getBlockState(position).isOf(Blocks.REDSTONE_WIRE) || mc.world.getBlockState(position).isOf(Blocks.TORCH)) {
                mc.interactionManager.attackBlock(position, Direction.UP);
                mc.player.swingHand(Hand.MAIN_HAND);
            } else {
                ((platform.inject.invokers.MinecraftClientInvoker) mc).invokeDoItemUse();
            }
        }
    }
}
