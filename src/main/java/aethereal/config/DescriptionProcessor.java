package aethereal.config;

import aethereal.autobuy.ItemFilter;
import aethereal.autobuy.ItemType;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DescriptionProcessor implements ItemFilter {
    private final List<DescriptionCondition> a = new ArrayList<>();

    public List<DescriptionCondition> b() {
        return this.a;
    }

    public void a(DescriptionCondition condition) {
        this.a.add(condition);
    }

    public DescriptionProcessor a(String description) {
        this.a.add(new DescriptionCondition(description));
        return this;
    }

    public DescriptionProcessor a(String description, int minLevel) {
        this.a.add(new DescriptionCondition(description, minLevel));
        return this;
    }

    public DescriptionProcessor a(String description, int minLevel, boolean enabled) {
        DescriptionCondition condition = new DescriptionCondition(description, minLevel);
        condition.a(enabled);
        this.a.add(condition);
        return this;
    }

    public DescriptionProcessor b(String description) {
        this.a.add(new DescriptionCondition(description, 0, ItemType.DENY));
        return this;
    }

    public DescriptionProcessor a() {
        return b("Попрыгун").b("Нестабильный ");
    }

    @Override
    public boolean a(ItemStack stack) {
        return this.a.stream().allMatch(condition -> {
            return condition.a(stack);
        });
    }
}
