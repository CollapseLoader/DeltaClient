package aethereal.util;

import aethereal.autobuy.AutoBuyEntry;
import aethereal.core.Interface;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InventoryUtil implements Interface {
    private InventoryUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ItemStack a(ItemStack stack) {
        EquippableComponent equippable = stack.get(DataComponentTypes.EQUIPPABLE);
        if (equippable != null && equippable.assetId().isPresent() && stack.getMaxDamage() > 0) {
            String percent = equippable.assetId().get().getValue().getPath();
            String percent2 = percent.substring(percent.lastIndexOf(95) + 1);
            if (percent2.matches("\\d+")) {
                ItemStack copy = stack.copy();
                copy.setDamage((copy.getMaxDamage() * (100 - Integer.parseInt(percent2))) / 100);
                return copy;
            }
        }
        return stack;
    }

    public static int a(Item item) {
        int count = 0;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static boolean a(ItemStack stack, RegistryKey<Enchantment> enchantment, int minLevel) {
        return stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT).getLevel(mc.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(enchantment)) >= minLevel;
    }

    public static boolean a(ItemStack stack, String needle) {
        return stack.getTooltip(Item.TooltipContext.DEFAULT, mc.player, TooltipType.BASIC).stream().anyMatch(line -> {
            return line.getString().contains(needle);
        });
    }

    public static int a(Item item, boolean hotbar) {
        int end = hotbar ? 9 : 36;
        for (int i = 0; i < end; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }

    public static int b(Item item) {
        return a(item, false);
    }

    public static int a(Item item, boolean hotbar, boolean simple) {
        int end = hotbar ? 9 : 36;
        for (int i = 0; i < end; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.getItem() == item && (!simple || !stack.hasGlint())) {
                return i;
            }
        }
        return -1;
    }

    public static int a(ItemStack stack, boolean hotbar) {
        if (stack != null && !stack.isEmpty()) {
            int slotLimit = hotbar ? 9 : 36;
            for (int slotIndex = 0; slotIndex < slotLimit; slotIndex++) {
                ItemStack candidate = mc.player.getInventory().getStack(slotIndex);
                if (!candidate.isEmpty() && candidate.getItem() == stack.getItem() && Objects.equals(candidate.get(DataComponentTypes.ATTRIBUTE_MODIFIERS), stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS))) {
                    return slotIndex;
                }
            }
            return -1;
        }
        return -1;
    }

    public static int b(ItemStack targetStack, boolean hotbar) {
        AutoBuyEntry info = Arrays.stream(AutoBuyEntry.values()).filter(i -> {
            return i.a(targetStack);
        }).findFirst().orElse(null);
        if (info == null) {
            return -1;
        }
        int direct = IntStream.range(0, hotbar ? 9 : 36).filter(slot -> {
            return info.a(mc.player.getInventory().getStack(slot));
        }).findFirst().orElse(-1);
        if (direct != -1 || hotbar) {
            return direct;
        }
        return IntStream.range(0, 36).filter(slot2 -> {
            BundleContentsComponent contents = mc.player.getInventory().getStack(slot2).get(DataComponentTypes.BUNDLE_CONTENTS);
            if (contents != null) {
                Stream<ItemStack> streamMethod_59707 = contents.stream();
                Objects.requireNonNull(info);
                return streamMethod_59707.anyMatch(info::a);
            }
            return false;
        }).findFirst().orElse(-1);
    }

    public static int a(ItemStack bundleStack, ItemStack targetStack) {
        AutoBuyEntry info = Arrays.stream(AutoBuyEntry.values()).filter(i -> {
            return i.a(targetStack);
        }).findFirst().orElse(null);
        BundleContentsComponent contents = bundleStack.get(DataComponentTypes.BUNDLE_CONTENTS);
        if (info == null || contents == null) {
            return -1;
        }
        return IntStream.range(0, contents.size()).filter(index -> {
            return info.a(contents.get(index));
        }).findFirst().orElse(-1);
    }

    public static int c(ItemStack targetStack, boolean hotbar) {
        AutoBuyEntry info = Arrays.stream(AutoBuyEntry.values()).filter(i -> {
            return i.a(targetStack);
        }).findFirst().orElse(null);
        if (info == null) {
            return 0;
        }
        return IntStream.range(0, hotbar ? 9 : 36).mapToObj(slot -> {
            return mc.player.getInventory().getStack(slot);
        }).mapToInt(stack -> {
            BundleContentsComponent contents = stack.get(DataComponentTypes.BUNDLE_CONTENTS);
            if (contents != null) {
                Stream<ItemStack> streamMethod_59707 = contents.stream();
                Objects.requireNonNull(info);
                return streamMethod_59707.filter(info::a).mapToInt(ItemStack::getCount).sum();
            }
            if (info.a(stack)) {
                return stack.getCount();
            }
            return 0;
        }).sum();
    }

    public static int c(Item item) {
        AttributeModifiersComponent modifiers;
        int fallbackSlot = -1;
        for (int i = 0; i < 45; i++) {
            if (i != 40 && mc.player.getInventory().getStack(i).getItem() == item) {
                if (mc.player.getInventory().getStack(i).contains(DataComponentTypes.ATTRIBUTE_MODIFIERS) && (modifiers = mc.player.getInventory().getStack(i).get(DataComponentTypes.ATTRIBUTE_MODIFIERS)) != null && !modifiers.modifiers().isEmpty()) {
                    return i;
                }
                if (fallbackSlot == -1) {
                    fallbackSlot = i;
                }
            }
        }
        return fallbackSlot;
    }

    public static int a() {
        Registry<Enchantment> registry = mc.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
        RegistryEntry.Reference<Enchantment> protection = registry.getOrThrow(Enchantments.PROTECTION);
        int bestSlot = -1;
        double bestScore = 0.0d;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);
            EquippableComponent equippable = stack.get(DataComponentTypes.EQUIPPABLE);
            if (equippable != null && equippable.slot() == EquipmentSlot.CHEST && !stack.isEmpty() && !stack.isOf(Items.ELYTRA)) {
                double score = EnchantmentHelper.getLevel(protection, stack);
                AttributeModifiersComponent mods = stack.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
                if (mods != null) {
                    for (AttributeModifiersComponent.Entry entry : mods.modifiers()) {
                        RegistryEntry<EntityAttribute> attribute = entry.attribute();
                        if (attribute == EntityAttributes.ARMOR || attribute == EntityAttributes.ARMOR_TOUGHNESS) {
                            score += entry.modifier().value();
                        }
                    }
                }
                if (score > bestScore) {
                    bestScore = score;
                    bestSlot = slot;
                }
            }
        }
        return bestSlot;
    }
}
