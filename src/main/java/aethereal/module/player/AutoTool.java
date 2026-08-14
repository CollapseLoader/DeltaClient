package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.handler.InventoryHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.hit.BlockHitResult;

import java.util.Comparator;
import java.util.stream.IntStream;

@ModuleRegister(name = "Auto Tool", description = "Автоматически выбирает подходящий инструмент для блока", category = Category.Player)
public class AutoTool extends Module {
    private final int[] b = {-1, -1};

    @EventTarget
    public void a(TickEvent event) {
        int bestSlot;
        InventoryHandler handler = Delta.getInstance().getModuleProcessor().v().a();
        if (handler.a().isEmpty()) {
            if (mc.crosshairTarget instanceof BlockHitResult hit) {
                if (mc.options.attackKey.isPressed()) {
                    if (this.b[0] != -1 || (bestSlot = a(mc.world.getBlockState(hit.getBlockPos()))) == -1) {
                        return;
                    }
                    this.b[0] = mc.player.getInventory().selectedSlot;
                    if (bestSlot > 8) {
                        this.b[1] = bestSlot;
                        handler.a(bestSlot, this.b[0], 1);
                        return;
                    } else {
                        mc.player.getInventory().selectedSlot = bestSlot;
                        return;
                    }
                }
            }
            if (this.b[0] == -1) {
                return;
            }
            if (this.b[1] == -1) {
                mc.player.getInventory().selectedSlot = this.b[0];
            } else {
                handler.a(this.b[1], this.b[0], 1);
            }
            this.b[0] = -1;
            this.b[1] = -1;
        }
    }

    private int a(BlockState state) {
        int shears;
        PlayerInventory inventory = mc.player.getInventory();
        return (!state.isOf(Blocks.COBWEB) || (shears = IntStream.range(0, inventory.main.size()).filter(i -> {
            return inventory.getStack(i).isOf(Items.SHEARS);
        }).findFirst().orElse(-1)) == -1) ? IntStream.range(0, inventory.main.size()).filter(i2 -> {
            return inventory.getStack(i2).getMiningSpeedMultiplier(state) > 1.0f && a(inventory.getStack(i2), state);
        }).boxed().max(Comparator.comparingDouble(i3 -> {
            return inventory.getStack(i3.intValue()).getMiningSpeedMultiplier(state);
        })).orElse(-1).intValue() : shears;
    }

    private boolean a(ItemStack stack, BlockState state) {
        if (state.isIn(BlockTags.AXE_MINEABLE)) {
            return stack.getItem() instanceof AxeItem;
        }
        if (state.isIn(BlockTags.PICKAXE_MINEABLE)) {
            return stack.getItem() instanceof PickaxeItem;
        }
        if (state.isIn(BlockTags.SHOVEL_MINEABLE)) {
            return stack.getItem() instanceof ShovelItem;
        }
        if (state.isIn(BlockTags.HOE_MINEABLE)) {
            return stack.getItem() instanceof HoeItem;
        }
        return true;
    }
}
