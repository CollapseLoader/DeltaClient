package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.Delta;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.BindSetting;
import aethereal.setting.ModeSetting;
import aethereal.ui.screen.SwapScreen;
import aethereal.util.InventoryUtil;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

@ModuleRegister(name = "Auto Swap", description = "Мгновенно перекладывает выбранные предметы во вторую руку по нажатию клавиши", category = Category.Combat)
public class AutoSwap extends Module {
    private final ModeSetting b = new ModeSetting("Режим перемещения", "Двойной", "Двойной", "Тройной");
    private final ModeSetting c = new ModeSetting("Первый предмет", "Сфера", "Сфера", "Тотем", "Золотое яблоко", "Щит").a(() -> {
        return Boolean.valueOf(this.b.l("Двойной"));
    });
    private final ModeSetting d = new ModeSetting("Второй предмет", "Тотем", "Сфера", "Тотем", "Золотое яблоко", "Щит").a(() -> {
        return Boolean.valueOf(this.b.l("Двойной"));
    });
    private final SwapScreen e = new SwapScreen(Text.literal("SwapMenu"));

    public AutoSwap() {
        BindSetting f = new BindSetting("Кнопка перемещения", 86, 0).a(() -> {
            if (Delta.getInstance().getModuleProcessor().t().V().b) {
                return;
            }
            if (this.b.l("Двойной")) {
                Delta.getInstance().getModuleProcessor().v().getInventoryHandler().moveItem(InventoryUtil.c(mc.player.getOffHandStack().getItem() == a(this.c) ? a(this.d) : a(this.c)), 45, 1);
            } else if (this.b.l("Тройной")) {
                mc.setScreen(this.e);
            }
        }).b(() -> {
            if (mc.currentScreen instanceof SwapScreen) {
                mc.setScreen(null);
            }
        });
        a(f, this.b, this.c, this.d);
    }

    public SwapScreen q() {
        return this.e;
    }

    private Item a(ModeSetting modeSetting) {
        switch (modeSetting.c()) {
            case "Сфера":
                return Items.PLAYER_HEAD;
            case "Тотем":
                return Items.TOTEM_OF_UNDYING;
            case "Золотое яблоко":
                return Items.GOLDEN_APPLE;
            case "Щит":
                return Items.SHIELD;
            default:
                return null;
        }
    }
}
