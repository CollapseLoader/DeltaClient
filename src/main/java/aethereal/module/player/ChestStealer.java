package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.ContainerEvent;
import aethereal.event.RayTraceEvent;
import aethereal.setting.BooleanSetting;
import aethereal.util.CounterUtil;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

@ModuleRegister(name = "Chest Stealer", description = "Автоматически забирает предметы из открытого сундука", category = Category.Player)
public class ChestStealer extends Module {
    private final BooleanSetting b = new BooleanSetting("Игнорировать сущностей", true);
    private final BooleanSetting c = new BooleanSetting("Авто-закрытие сундука", true);
    private final CounterUtil d = new CounterUtil();

    public ChestStealer() {
        a(this.b, this.c);
    }

    public BooleanSetting q() {
        return this.b;
    }

    public BooleanSetting r() {
        return this.c;
    }

    public CounterUtil s() {
        return this.d;
    }

    @EventTarget
    public void a(RayTraceEvent event) {
        if (this.b.c().booleanValue()) {
            event.a(true);
        }
    }

    @EventTarget
    public void a(ContainerEvent event) {
        Slot target;
        if (event.h() == ContainerEvent.Phase.POST) {
            if (((event.getScreen() instanceof GenericContainerScreen) || (event.getScreen() instanceof ShulkerBoxScreen)) && (target = event.e().stream().filter(slot -> {
                return slot.inventory != mc.player.getInventory();
            }).filter((v0) -> {
                return v0.hasStack();
            }).findFirst().orElse(null)) != null && this.d.a(5L, 5L)) {
                mc.interactionManager.clickSlot(event.getHandler().syncId, target.id, 0, SlotActionType.QUICK_MOVE, mc.player);
                boolean empty = event.e().stream().filter(slot2 -> {
                    return slot2.inventory != mc.player.getInventory();
                }).noneMatch(slot3 -> {
                    return slot3.hasStack() && slot3 != target;
                });
                if (empty && this.c.c().booleanValue()) {
                    mc.player.closeScreen();
                }
                this.d.b();
            }
        }
    }
}
