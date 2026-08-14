package aethereal.config;

import aethereal.autobuy.ItemFilter;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PotionProcessor implements ItemFilter {
    private final List<PotionCondition> a = new ArrayList<>();

    public List<PotionCondition> a() {
        return this.a;
    }

    public PotionProcessor a(PotionCondition condition) {
        this.a.add(condition);
        return this;
    }

    @Override
    public boolean a(ItemStack stack) {
        return this.a.stream().allMatch(condition -> {
            return condition.a(stack);
        });
    }
}
