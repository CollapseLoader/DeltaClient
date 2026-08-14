package aethereal.module.misc;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.Hand;

@ModuleRegister(a = "Anti AFK", b = "Не даёт серверу кикнуть вас за бездействие", c = Category.Player)
public class AntiAFK extends Module {
    private final ModeSetting b = new ModeSetting("Режим использования", "Обычный", "Обычный", "FunTime");
    private final MultiModeSetting c = new MultiModeSetting("Выполнять действия", new BooleanSetting("Прыжок", true), new BooleanSetting("Взмах", true), new BooleanSetting("Движение", true)).a(() -> {
        return Boolean.valueOf(this.b.l("Обычный"));
    });
    private final BooleanSetting d = new BooleanSetting("Реагировать на недоступность", false).a(() -> {
        return Boolean.valueOf(this.b.l("FunTime"));
    });

    public AntiAFK() {
        a(this.b, this.c, this.d);
    }

    @EventTarget
    public void a(InputEvent event) {
        if (mc.player.age % 600 == 0) {
            if (this.b.l("Обычный")) {
                if (this.c.a("Прыжок").c().booleanValue() && mc.player.isOnGround()) {
                    event.b(true);
                }
                if (this.c.a("Взмах").c().booleanValue()) {
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
                if (this.c.a("Движение").c().booleanValue()) {
                    Delta.h().d().v().g().a(7);
                    return;
                }
                return;
            }
            if (this.b.l("FunTime") && !this.d.c().booleanValue()) {
                Delta.h().d().v().g().a(7);
            }
        }
    }

    @EventTarget
    public void a(PacketEvent event) {
        if (this.b.l("FunTime") && this.d.c().booleanValue() && event.c()) {
            GameMessageS2CPacket message = (GameMessageS2CPacket) event.d();
            if (message instanceof GameMessageS2CPacket) {
                if (message.content().getString().equals("Данная команда недоступна в режиме AFK")) {
                    Delta.h().d().v().g().a(7);
                }
            }
        }
    }
}
