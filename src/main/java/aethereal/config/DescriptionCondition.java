package aethereal.config;

import aethereal.autobuy.ItemType;
import aethereal.core.Interface;
import aethereal.render.AnimationUtil;
import aethereal.util.MathUtil;
import aethereal.util.StringUtils;
import lombok.Generated;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;

import java.util.stream.Collectors;

public class DescriptionCondition implements Condition {
    private final AnimationUtil a;
    private final String b;
    private ItemType c;
    private int d;

    public DescriptionCondition(String description) {
        this(description, 0, ItemType.ON);
    }

    public DescriptionCondition(String description, int requiredLevel) {
        this(description, requiredLevel, ItemType.ON);
    }

    public DescriptionCondition(String description, int requiredLevel, ItemType type) {
        this.a = new AnimationUtil();
        this.b = description;
        this.c = type;
        this.d = (type != ItemType.ON || requiredLevel <= 0) ? requiredLevel : Math.min(j(), requiredLevel);
    }

    @Override
    @Generated
    public AnimationUtil a() {
        return this.a;
    }

    @Generated
    public String i() {
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
        return this.b;
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
        this.d = Math.max(1, Math.min(j(), this.d + delta));
    }

    public boolean a(ItemStack stack) {
        int iA;
        if (this.c == ItemType.OFF) {
            return true;
        }
        String tooltip = stack.getTooltip(Item.TooltipContext.DEFAULT, Interface.mc.player, TooltipType.BASIC).stream().skip(1L).map(line -> {
            return line.getString().replaceAll("§.", "").toLowerCase().replaceAll("\\s+", StringUtils.a).trim();
        }).collect(Collectors.joining(StringUtils.a));
        String needle = this.b.replaceAll("§.", "").toLowerCase().replaceAll("\\s+", StringUtils.a).trim();
        if (this.c == ItemType.DENY) {
            return !tooltip.contains(needle);
        }
        if (!tooltip.contains(needle)) {
            return false;
        }
        if (this.d == 0) {
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
        return Math.max(1, level) >= this.d;
    }

    @Override
    public String e() {
        return this.d == 0 ? this.b : this.b + " " + MathUtil.a(this.d - 1);
    }

    private int j() {
        return ("Окисление".equals(this.b) || "Вампиризм".equals(this.b)) ? 2 : 3;
    }
}
