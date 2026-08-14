package aethereal.config;

import aethereal.autobuy.ItemType;
import aethereal.core.Interface;
import aethereal.render.AnimationUtil;
import aethereal.util.MathUtil;
import aethereal.util.StringUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;

import java.util.stream.Collectors;

public class DescriptionCondition implements Condition {
    private final AnimationUtil animation;
    private final String description;
    private ItemType type;
    private int requiredLevel;

    public DescriptionCondition(String description) {
        this(description, 0, ItemType.ON);
    }

    public DescriptionCondition(String description, int requiredLevel) {
        this(description, requiredLevel, ItemType.ON);
    }

    public DescriptionCondition(String description, int requiredLevel, ItemType type) {
        this.animation = new AnimationUtil();
        this.description = description;
        this.type = type;
        this.requiredLevel = (type != ItemType.ON || requiredLevel <= 0) ? requiredLevel : Math.min(j(), requiredLevel);
    }

    @Override
    public AnimationUtil a() {
        return this.animation;
    }

    public String i() {
        return this.description;
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
        return this.description;
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
        this.requiredLevel = Math.max(1, Math.min(j(), this.requiredLevel + delta));
    }

    public boolean a(ItemStack stack) {
        int iA;
        if (this.type == ItemType.OFF) {
            return true;
        }
        String tooltip = stack.getTooltip(Item.TooltipContext.DEFAULT, Interface.mc.player, TooltipType.BASIC).stream().skip(1L).map(line -> {
            return line.getString().replaceAll("§.", "").toLowerCase().replaceAll("\\s+", StringUtils.a).trim();
        }).collect(Collectors.joining(StringUtils.a));
        String needle = this.description.replaceAll("§.", "").toLowerCase().replaceAll("\\s+", StringUtils.a).trim();
        if (this.type == ItemType.DENY) {
            return !tooltip.contains(needle);
        }
        if (!tooltip.contains(needle)) {
            return false;
        }
        if (this.requiredLevel == 0) {
            return true;
        }
        String after = tooltip.substring(tooltip.indexOf(needle) + needle.length()).trim();
        int space = after.indexOf(32);
        if (after.isEmpty()) {
            iA = 0;
        } else {
            iA = MathUtil.a(space > 0 ? after.substring(0, space) : after);
        }
        int level = iA;
        return Math.max(1, level) >= this.requiredLevel;
    }

    @Override
    public String e() {
        return this.requiredLevel == 0 ? this.description : this.description + " " + MathUtil.a(this.requiredLevel - 1);
    }

    private int j() {
        return ("Окисление".equals(this.description) || "Вампиризм".equals(this.description)) ? 2 : 3;
    }
}
