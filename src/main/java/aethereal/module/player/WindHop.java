package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import lombok.Generated;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;

@ModuleRegister(a = "Wind Hop", b = "Автоматически прыгает после использования заряда ветра", c = Category.Player)
public class WindHop extends Module implements Interface {
    private final BooleanSetting b = new BooleanSetting("Поворачивать голову вниз", true);
    private int c = -1;

    public WindHop() {
        a(this.b);
    }

    @Generated
    public BooleanSetting q() {
        return this.b;
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (event.b()) {
            PlayerInteractItemC2SPacket class_2886VarD = (PlayerInteractItemC2SPacket) event.d();
            if (class_2886VarD instanceof PlayerInteractItemC2SPacket) {
                PlayerInteractItemC2SPacket packet = class_2886VarD;
                if (aM_.player.getStackInHand(packet.getHand()).isOf(Items.WIND_CHARGE)) {
                    this.c = 2;
                }
            }
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.c > 0) {
            this.c--;
        }
    }

    @EventTarget
    public void a(InputEvent event) {
        if (this.c == 0) {
            event.b(true);
            this.c = -1;
        }
    }
}
