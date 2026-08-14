package aethereal.config;


import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.stream.StreamSupport;

public record PotionCondition(RegistryEntry<StatusEffect> a, int b, int c) {
    public PotionCondition {
        if (a == null) {
            throw new IllegalArgumentException("Effect cannot be null");
        }
    }

    @Override
    public RegistryEntry<StatusEffect> a() {
        return this.a;
    }

    @Override
    public int b() {
        return this.b;
    }

    @Override
    public int c() {
        return this.c;
    }

    public boolean a(ItemStack stack) {
        PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (contents == null) {
            return false;
        }
        return StreamSupport.stream(contents.getEffects().spliterator(), false).anyMatch(effect -> {
            return effect.getEffectType().equals(this.a) && effect.getAmplifier() + 1 == this.b && effect.getDuration() >= this.c;
        });
    }

    public String toString() {
        return "PotionCondition{effect=" + this.a.getKey().map(k -> {
            return k.getValue().toString();
        }).orElse("unknown") + ", requiredLevel=" + this.b + ", requiredDuration=" + this.c + "}";
    }
}
