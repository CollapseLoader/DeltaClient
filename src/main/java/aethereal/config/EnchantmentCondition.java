package aethereal.config;

import aethereal.autobuy.ItemType;
import aethereal.core.Interface;
import aethereal.render.AnimationUtil;
import lombok.Generated;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public class EnchantmentCondition implements Condition {
    private final AnimationUtil a;
    private final RegistryKey<Enchantment> b;
    private ItemType c;
    private int d;

    public EnchantmentCondition(RegistryKey<Enchantment> key) {
        this(key, 0, ItemType.ON);
    }

    public EnchantmentCondition(RegistryKey<Enchantment> key, int requiredLevel) {
        this(key, requiredLevel, ItemType.ON);
    }

    public EnchantmentCondition(RegistryKey<Enchantment> key, int requiredLevel, ItemType type) {
        this.a = new AnimationUtil();
        this.b = key;
        this.c = type;
        this.d = type == ItemType.ON ? c(requiredLevel) : requiredLevel;
    }

    @Override
    @Generated
    public AnimationUtil a() {
        return this.a;
    }

    @Generated
    public RegistryKey<Enchantment> i() {
        return this.b;
    }

    @Override
    @Generated
    public void a(ItemType type) {
        this.c = type;
    }

    @Override
    @Generated
    public ItemType h() {
        return this.c;
    }

    @Override
    @Generated
    public void a(int requiredLevel) {
        this.d = requiredLevel;
    }

    @Override
    @Generated
    public int g() {
        return this.d;
    }

    @Override
    public String f() {
        return this.b.getValue().toString();
    }

    @Override
    public boolean b() {
        return this.c != ItemType.OFF;
    }

    @Override
    public void a(boolean enabled) {
        this.c = enabled ? ItemType.ON : ItemType.OFF;
    }

    @Override
    public boolean c() {
        return this.d > 0;
    }

    @Override
    public boolean d() {
        return this.c == ItemType.DENY;
    }

    @Override
    public void b(int delta) {
        if (this.c != ItemType.ON || this.d == 0) {
            return;
        }
        this.d = Math.max(1, c(this.d + delta));
    }

    public boolean a(ItemStack stack) {
        if (this.c == ItemType.OFF) {
            return true;
        }
        RegistryEntry.Reference class_6883VarMethod_46747 = Interface.aM_.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(this.b);
        ItemEnchantmentsComponent enchantments = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT);
        int level = enchantments.getLevel(class_6883VarMethod_46747);
        int threshold = this.d == 0 ? 1 : this.d;
        if (this.c == ItemType.DENY) {
            return level < threshold;
        }
        return !enchantments.isEmpty() && level >= threshold;
    }

    @Override
    public String e() {
        return Text.translatable("enchantment." + this.b.getValue().getNamespace() + "." + this.b.getValue().getPath().replace("/", ".")).getString() + (this.d == 0 ? "" : " " + this.d);
    }

    private int c(int level) {
        int iMethod_8183;
        if (level <= 0) {
            return 0;
        }
        if (Interface.aM_.world == null || !Interface.aM_.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).contains(this.b)) {
            return level;
        }
        if (this.b.equals(Enchantments.SHARPNESS)) {
            iMethod_8183 = 7;
        } else {
            iMethod_8183 = (this.b.equals(Enchantments.PROTECTION) || this.b.equals(Enchantments.UNBREAKING)) ? 5 : Interface.aM_.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(this.b).value().getMaxLevel();
        }
        return Math.max(1, Math.min(iMethod_8183, level));
    }
}
