package aethereal.module.misc;

import aethereal.autobuy.AutoBuyEntry;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.setting.BindSetting;
import aethereal.setting.ModeSetting;
import aethereal.ui.screen.AssistantScreen;
import lombok.Generated;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

@ModuleRegister(a = "Potion Thrower", b = "Быстрое метание бафов через колесо или по клавише", c = Category.Misc)
public class PotionThrower extends Module implements Interface {
    private final ModeSetting b = new ModeSetting("Способ использования зелий", "Колесо выбора", "Колесо выбора", "Клавиша");
    private final AssistantScreen c = new AssistantScreen(Text.literal("Potion Thrower"));

    public PotionThrower() {
        BindSetting d = new BindSetting("Открыть меню зелий", 86, 0).a(() -> {
            aM_.setScreen(this.c);
        }).b(() -> {
            if (aM_.currentScreen == this.c) {
                this.c.b(this.c.b());
                if (aM_.currentScreen == this.c) {
                    aM_.setScreen(null);
                }
            }
        }).a(() -> {
            return Boolean.valueOf(this.b.l("Колесо выбора"));
        });
        a(this.b, d);
        for (AutoBuyEntry potion : AutoBuyEntry.values()) {
            if (potion.d() == Items.SPLASH_POTION) {
                a(new BindSetting(potion.b(), -1).a(() -> {
                    Delta.h().d().v().b().a(potion.a());
                }).a(() -> {
                    return Boolean.valueOf(this.b.l("Клавиша"));
                }));
            }
        }
    }

    @Generated
    public AssistantScreen q() {
        return this.c;
    }
}
