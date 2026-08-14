package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.StringSetting;
import aethereal.util.ServerUtil;
import lombok.Generated;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

@ModuleRegister(a = "Auto Auth", b = "Автоматически вводит пароль при авторизации и регистрации", c = Category.Player)
public class AutoAuth extends Module implements Interface {
    private final StringSetting b = new StringSetting("Пароль авторизации", "").a();
    private final StringSetting c = new StringSetting("Пароль регистрации", "").a();
    private String d;

    public AutoAuth() {
        a(this.b, this.c);
    }

    @Generated
    public StringSetting q() {
        return this.b;
    }

    @Generated
    public StringSetting r() {
        return this.c;
    }

    @Generated
    public String s() {
        return this.d;
    }

    @EventTarget
    public void a(PacketEvent eventPacket) {
        if (eventPacket.c()) {
            if (eventPacket.d() instanceof GameMessageS2CPacket packet) {
                String message = packet.content().getString();
                if ((message.contains("Зарегистрируйтесь") || message.contains("/reg") || message.contains("/register")) && !this.c.c().isEmpty()) {
                    this.d = "/reg " + this.c.c();
                }
                if ((message.contains("Авторизуйтесь") || message.contains("Войдите в игру") || message.contains("/login")) && !this.b.c().isEmpty()) {
                    this.d = "/login " + this.b.c();
                }
            }
        }
    }

    @EventTarget
    public void a(TickEvent tickEvent) {
        if (this.d != null) {
            if (ServerUtil.a.a() && ServerUtil.a.c()) {
                return;
            }
            mc.player.networkHandler.sendChatMessage(this.d);
            this.d = null;
        }
    }
}
