package aethereal.config;

import aethereal.autobuy.ItemFilter;
import aethereal.autobuy.ItemType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentProcessor implements ItemFilter {
    private final List<EnchantmentCondition> a = new ArrayList<>();

    public List<EnchantmentCondition> b() {
        return this.a;
    }

    public EnchantmentProcessor a(EnchantmentCondition condition) {
        this.a.add(condition);
        return this;
    }

    public EnchantmentProcessor a(RegistryKey<Enchantment> key) {
        return a(new EnchantmentCondition(key));
    }

    public EnchantmentProcessor a(RegistryKey<Enchantment> key, int requiredLevel) {
        return a(new EnchantmentCondition(key, requiredLevel));
    }

    public EnchantmentProcessor b(RegistryKey<Enchantment> key) {
        return a(new EnchantmentCondition(key, 0, ItemType.DENY));
    }

    public EnchantmentProcessor b(RegistryKey<Enchantment> key, int maxLevel) {
        return a(new EnchantmentCondition(key, maxLevel, ItemType.DENY));
    }

    public EnchantmentProcessor a() {
        return b(Enchantments.THORNS).b(Enchantments.KNOCKBACK).b(Enchantments.BINDING_CURSE)
                .b(Enchantments.VANISHING_CURSE);
    }

    @Override
    public boolean a(ItemStack stack) {
        return this.a.stream().allMatch(condition -> {
            return condition.a(stack);
        });
    }
}
