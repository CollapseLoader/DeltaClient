package aethereal.config;

import aethereal.autobuy.ItemFilter;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class NBTProcessor implements ItemFilter {
    private final List<NBTCondition> a = new ArrayList<>();

    public List<NBTCondition> a() {
        return this.a;
    }

    public NBTProcessor a(NBTCondition condition) {
        this.a.add(condition);
        return this;
    }

    public NBTProcessor a(String value) {
        return a(new NBTCondition(value));
    }

    @Override
    public boolean a(ItemStack stack) {
        return this.a.stream().allMatch(condition -> {
            return condition.a(stack);
        });
    }
}
