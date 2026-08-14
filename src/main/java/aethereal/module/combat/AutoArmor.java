package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.BundleItemSelectedC2SPacket;
import net.minecraft.registry.RegistryKeys;

@ModuleRegister(name = "Auto Armor", description = "Автоматически надевает лучшую броню из инвентаря и мешков", category = Category.Combat)
public class AutoArmor extends Module implements Interface {
    private final BooleanSetting b = new BooleanSetting("Не в движении", true);
    private final EquipmentSlot[] c = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private int d;

    public AutoArmor() {
        a(this.b);
    }

    @Override
    public void b() {
        super.b();
        this.d = 0;
    }

    @Override
    public void c() {
        super.c();
        this.d = 0;
    }

    @EventTarget
    public void a(TickEvent event) {
        if (!Delta.h().d().v().a().a().isEmpty()) {
            return;
        }
        this.d++;
        boolean urgent = a(mc.player.getEquippedStack(EquipmentSlot.HEAD)) || a(mc.player.getEquippedStack(EquipmentSlot.CHEST)) || a(mc.player.getEquippedStack(EquipmentSlot.LEGS)) || a(mc.player.getEquippedStack(EquipmentSlot.FEET));
        if (!urgent) {
            if (this.d % 2 != 0) {
                return;
            }
            if (this.b.c().booleanValue() && mc.player.getVelocity().horizontalLengthSquared() > 9.99999713651348E-5d) {
                return;
            }
        }
        for (int armorIndex = 0; armorIndex < this.c.length && !a(this.c[armorIndex], armorIndex); armorIndex++) {
        }
    }

    private boolean a(EquipmentSlot slot, int armorIndex) {
        ItemStack current = mc.player.getEquippedStack(slot);
        boolean low = a(current);
        double best = low ? b(current) + 20 : c(current);
        int bestSlot = -1;
        int bestBundle = -1;
        for (int inventorySlot = 0; inventorySlot < 36; inventorySlot++) {
            ItemStack stack = mc.player.getInventory().getStack(inventorySlot);
            if (a(stack, slot)) {
                double value = low ? b(stack) : c(stack);
                if (value > best) {
                    best = value;
                    bestSlot = inventorySlot;
                    bestBundle = -1;
                }
            }
            BundleContentsComponent contents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
            if (contents != null) {
                for (int bundleIndex = 0; bundleIndex < contents.size(); bundleIndex++) {
                    ItemStack bundled = contents.get(bundleIndex);
                    if (a(bundled, slot)) {
                        double value2 = low ? b(bundled) : c(bundled);
                        if (value2 > best) {
                            best = value2;
                            bestSlot = inventorySlot;
                            bestBundle = bundleIndex;
                        }
                    }
                }
            }
        }
        if (bestSlot == -1) {
            return false;
        }
        if (bestBundle != -1) {
            mc.player.networkHandler.sendPacket(new BundleItemSelectedC2SPacket(bestSlot < 9 ? 36 + bestSlot : bestSlot, bestBundle));
        }
        Delta.h().d().v().a().b(bestSlot, armorIndex, 1);
        return true;
    }

    private boolean a(ItemStack stack) {
        return !stack.isEmpty() && stack.getMaxDamage() > 0 && b(stack) < 41;
    }

    private boolean a(ItemStack stack, EquipmentSlot slot) {
        EquippableComponent equippable;
        return !stack.isEmpty() && (equippable = stack.get(DataComponentTypes.EQUIPPABLE)) != null && equippable.slot() == slot && (slot != EquipmentSlot.CHEST || !stack.isOf(Items.ELYTRA));
    }

    private int b(ItemStack stack) {
        return stack.getMaxDamage() - stack.getDamage();
    }

    private double c(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0.0d;
        }
        double score = EnchantmentHelper.getLevel(mc.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.PROTECTION), stack);
        AttributeModifiersComponent modifiers = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
        if (modifiers != null) {
            for (AttributeModifiersComponent.Entry entry : modifiers.modifiers()) {
                if (entry.attribute() == EntityAttributes.ARMOR || entry.attribute() == EntityAttributes.ARMOR_TOUGHNESS) {
                    score += entry.modifier().value();
                }
            }
        }
        return score;
    }
}
