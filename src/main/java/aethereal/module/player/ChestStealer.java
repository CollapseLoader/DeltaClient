package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.ContainerEvent;
import aethereal.event.RayTraceEvent;
import aethereal.setting.BooleanSetting;
import aethereal.util.CounterUtil;
import lombok.Generated;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

@ModuleRegister(a = "Chest Stealer", b = "Автоматически забирает предметы из открытого сундука", c = Category.Player)
public class ChestStealer extends Module implements Interface {
    private final BooleanSetting b = new BooleanSetting("Игнорировать сущностей", true);
    private final BooleanSetting c = new BooleanSetting("Авто-закрытие сундука", true);
    private final CounterUtil d = new CounterUtil();

    public ChestStealer() {
        a(this.b, this.c);
    }

    @Generated
    public BooleanSetting q() {
        return this.b;
    }

    @Generated
    public BooleanSetting r() {
        return this.c;
    }

    @Generated
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
            if (((event.b() instanceof GenericContainerScreen) || (event.b() instanceof ShulkerBoxScreen)) && (target = event.e().stream().filter(slot -> {
                return slot.inventory != aM_.player.getInventory();
            }).filter((v0) -> {
                return v0.hasStack();
            }).findFirst().orElse(null)) != null && this.d.a(5L, 5L)) {
                aM_.interactionManager.clickSlot(event.c().syncId, target.id, 0, SlotActionType.QUICK_MOVE, aM_.player);
                boolean empty = event.e().stream().filter(slot2 -> {
                    return slot2.inventory != aM_.player.getInventory();
                }).noneMatch(slot3 -> {
                    return slot3.hasStack() && slot3 != target;
                });
                if (empty && this.c.c().booleanValue()) {
                    aM_.player.closeScreen();
                }
                this.d.b();
            }
        }
    }
}
