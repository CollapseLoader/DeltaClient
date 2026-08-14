package aethereal.config;


import aethereal.core.Interface;
import aethereal.util.StringUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;

public record NBTCondition(String a) {

    @Override
    public String a() {
        return this.a;
    }

    public boolean a(ItemStack stack) {
        NbtElement nbt = stack.toNbt(Interface.mc.world.getRegistryManager());
        return nbt != null && nbt.toString().replaceAll("§.", "").toLowerCase().replaceAll("\\s+", StringUtils.a).trim().contains(this.a.replaceAll("§.", "").toLowerCase().replaceAll("\\s+", StringUtils.a).trim());
    }
}
