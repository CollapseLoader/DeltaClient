package aethereal.module.combat;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;
import aethereal.util.InventoryUtil;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@ModuleRegister(name = "Auto Totem", description = "Берёт тотем бессмертия в руку при падении здоровья ниже заданного значения", category = Category.Combat)
public class AutoTotem extends Module {
    private final SliderSetting c = new SliderSetting("Порог здоровья", 6.0f, 1.0f, 20.0f, 0.5f);
    private final MultiModeSetting d = new MultiModeSetting("Дополнительные опции", new BooleanSetting("Возвращать предмет", true), new BooleanSetting("Сначала обычные тотемы", true), new BooleanSetting("Не во время еды", false));
    private final BooleanSetting e = new BooleanSetting("Умное перемещение", true);
    private final CounterUtil f = new CounterUtil();
    private final float[] g = new float[20];
    public boolean b;
    private int h = -1;

    public AutoTotem() {
        a(this.c, this.d, this.e);
    }

    @Override
    public void b() {
        super.b();
        this.h = -1;
    }

    @Override
    public void c() {
        super.c();
        this.h = -1;
        this.b = false;
    }

    @EventTarget
    public void a(TickEvent event) {
        System.arraycopy(this.g, 0, this.g, 1, 19);
        this.g[0] = mc.player.getHealth() + (mc.player.hasStatusEffect(StatusEffects.ABSORPTION) ? mc.player.getAbsorptionAmount() : 0.0f);
        if (this.d.a("Не во время еды").c().booleanValue() && t()) {
            return;
        }
        Boolean should = s();
        this.b = should != null ? should.booleanValue() : this.b;
        if (should != null) {
            if (should.booleanValue()) {
                if (!this.f.a(400L) || !q()) {
                    return;
                }
            } else if (!r()) {
                return;
            }
            this.f.b();
        }
    }

    private boolean q() {
        ItemStack offhand = mc.player.getOffHandStack();
        boolean preferPlain = this.d.a("Сначала обычные тотемы").c().booleanValue();
        if (a(offhand) && (!preferPlain || !offhand.hasGlint())) {
            return false;
        }
        int slot = preferPlain ? InventoryUtil.a(Items.TOTEM_OF_UNDYING, false, true) : -1;
        if (slot == -1 && !a(offhand)) {
            slot = InventoryUtil.b(Items.TOTEM_OF_UNDYING);
        }
        if (slot == -1) {
            return false;
        }
        if (this.d.a("Возвращать предмет").c().booleanValue() && this.h == -1 && !offhand.isEmpty()) {
            this.h = slot;
        }
        Delta.getInstance().getModuleProcessor().v().a().a(slot, 40, 1);
        return true;
    }

    private boolean r() {
        ItemStack offhand = mc.player.getOffHandStack();
        if (this.d.a("Возвращать предмет").c().booleanValue() && this.h != -1 && (offhand.isEmpty() || a(offhand))) {
            Delta.getInstance().getModuleProcessor().v().a().a(this.h, 40, 1);
        }
        this.h = -1;
        return false;
    }

    private Boolean s() {
        if (mc.player.getItemCooldownManager().isCoolingDown(Items.TOTEM_OF_UNDYING.getDefaultStack())) {
            return false;
        }
        ItemStack active = mc.player.getActiveItem();
        if (this.e.c().booleanValue() && t() && active.getItem().getMaxUseTime(active, mc.player) > 5) {
            int left = mc.player.getItemUseTimeLeft();
            if (left < 10) {
                return null;
            }
            if (left < 30 && ((this.g[19] - this.g[0]) / 20.0f) * left < this.g[0]) {
                return null;
            }
        }
        if (this.g[0] <= this.c.c().floatValue()) {
            return true;
        }
        return this.g[0] >= this.c.c().floatValue() + 0.5f ? false : null;
    }

    private boolean t() {
        ItemStack active = mc.player.getActiveItem();
        return mc.player.isUsingItem() && !active.isEmpty() && active.get(DataComponentTypes.FOOD) != null;
    }

    private boolean a(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() == Items.TOTEM_OF_UNDYING;
    }
}
