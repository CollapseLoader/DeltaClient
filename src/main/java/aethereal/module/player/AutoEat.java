package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.setting.SliderSetting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Set;

@ModuleRegister(a = "Auto Eat", b = "Автоматически утоляет голод при его падении", c = Category.Player)
public class AutoEat extends Module {
    private final SliderSetting b = new SliderSetting("Есть при голоде", 16.0f, 1.0f, 20.0f, 1.0f);

    public AutoEat() {
        a(this.b);
    }

    @EventTarget
    public void a(TickEvent event) {
        b(this.b.c().intValue());
    }

    public void b(int level) {
        int slot;
        if (mc.player != null && mc.player.getHungerManager().getFoodLevel() < level && (slot = q()) >= 0) {
            Delta.h().d().v().k().a(slot);
        }
    }

    private int q() {
        int i;
        Set<Item> blacklist = Set.of(Items.CHORUS_FRUIT, Items.PUFFERFISH, Items.DRIED_KELP);
        Set<Item> raw = Set.of(Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.MUTTON, Items.RABBIT, Items.COD, Items.SALMON, Items.POTATO);
        int bestSlot = -1;
        int bestRank = Integer.MAX_VALUE;
        for (int slot = 0; slot < 36 && bestRank > 0; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (stack.contains(DataComponentTypes.FOOD) && !blacklist.contains(stack.getItem()) && !stack.contains(DataComponentTypes.CUSTOM_NAME)) {
                if (stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) {
                    i = 3;
                } else if (stack.isOf(Items.GOLDEN_APPLE)) {
                    i = 2;
                } else {
                    i = raw.contains(stack.getItem()) ? 1 : 0;
                }
                int rank = i;
                if (rank < bestRank) {
                    bestRank = rank;
                    bestSlot = slot;
                }
            }
        }
        return bestSlot;
    }
}
