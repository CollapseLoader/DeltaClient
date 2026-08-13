package aethereal.config;

import aethereal.autobuy.ItemFilter;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class AttributeProcessor implements ItemFilter {
    private final List<AttributeCondition> a = new ArrayList();

    public AttributeProcessor a(AttributeCondition condition) {
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
