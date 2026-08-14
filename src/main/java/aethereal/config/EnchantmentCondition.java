package aethereal.config;

import aethereal.autobuy.ItemType;
import aethereal.core.Interface;
import aethereal.render.AnimationUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.text.Text;

public class EnchantmentCondition implements Condition {
    private final AnimationUtil animation;
    private final RegistryKey<Enchantment> enchantmentKey;
    private ItemType type;
    private int requiredLevel;

    public EnchantmentCondition(RegistryKey<Enchantment> key) {
        this(key, 0, ItemType.ON);
    }

    public EnchantmentCondition(RegistryKey<Enchantment> key, int requiredLevel) {
        this(key, requiredLevel, ItemType.ON);
    }

    public EnchantmentCondition(RegistryKey<Enchantment> key, int requiredLevel, ItemType type) {
        this.animation = new AnimationUtil();
        this.enchantmentKey = key;
        this.type = type;
        this.requiredLevel = type == ItemType.ON ? c(requiredLevel) : requiredLevel;
    }

    @Override
    public AnimationUtil a() {
        return this.animation;
    }

    public RegistryKey<Enchantment> i() {
        return this.enchantmentKey;
    }

    @Override
    public void a(ItemType type) {
        this.type = type;
    }

    @Override
    public ItemType h() {
        return this.type;
    }

    @Override
    public void a(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    @Override
    public int g() {
        return this.requiredLevel;
    }

    @Override
    public String f() {
        return this.enchantmentKey.getValue().toString();
    }

    @Override
    public boolean b() {
        return this.type != ItemType.OFF;
    }

    @Override
    public void a(boolean enabled) {
        this.type = enabled ? ItemType.ON : ItemType.OFF;
    }

    @Override
    public boolean c() {
        return this.requiredLevel > 0;
    }

    @Override
    public boolean d() {
        return this.type == ItemType.DENY;
    }

    @Override
    public void b(int delta) {
        if (this.type != ItemType.ON || this.requiredLevel == 0) {
            return;
        }
        this.requiredLevel = Math.max(1, c(this.requiredLevel + delta));
    }

    public boolean a(ItemStack stack) {
        if (this.type == ItemType.OFF) {
            return true;
        }
        Reference<Enchantment> class_6883VarMethod_46747 = Interface.mc.world.getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(this.enchantmentKey);
        ItemEnchantmentsComponent enchantments = stack.getOrDefault(DataComponentTypes.ENCHANTMENTS,
                ItemEnchantmentsComponent.DEFAULT);
        int level = enchantments.getLevel(class_6883VarMethod_46747);
        int threshold = this.requiredLevel == 0 ? 1 : this.requiredLevel;
        if (this.type == ItemType.DENY) {
            return level < threshold;
        }
        return !enchantments.isEmpty() && level >= threshold;
    }

    @Override
    public String e() {
        return Text
                .translatable("enchantment." + this.enchantmentKey.getValue().getNamespace() + "."
                        + this.enchantmentKey.getValue().getPath().replace("/", "."))
                .getString() + (this.requiredLevel == 0 ? "" : " " + this.requiredLevel);
    }

    private int c(int level) {
        int iMethod_8183;
        if (level <= 0) {
            return 0;
        }
        if (Interface.mc.world == null
                || !Interface.mc.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).contains(this.enchantmentKey)) {
            return level;
        }
        if (this.enchantmentKey.equals(Enchantments.SHARPNESS)) {
            iMethod_8183 = 7;
        } else {
            iMethod_8183 = (this.enchantmentKey.equals(Enchantments.PROTECTION) || this.enchantmentKey.equals(Enchantments.UNBREAKING)) ? 5
                    : Interface.mc.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(this.enchantmentKey)
                    .value().getMaxLevel();
        }
        return Math.max(1, Math.min(iMethod_8183, level));
    }
}
