package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import aethereal.util.ServerUtil;
import net.minecraft.entity.player.PlayerEntity;

@ModuleRegister(name = "Auto Leave", description = "Автоматически выходит в хаб по триггерам", category = Category.Player)
public class AutoLeave extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Условия срабатывания", new BooleanSetting("Малое ХП", true), new BooleanSetting("Игроки рядом", true));
    private final SliderSetting c = new SliderSetting("Минимум ХП", 8.0f, 1.0f, 20.0f, 0.5f).a(() -> {
        return this.b.a("Малое ХП").c();
    });
    private final SliderSetting d = new SliderSetting("Дистанция игроков", 8.0f, 8.0f, 128.0f, 1.0f).a(() -> {
        return this.b.a("Игроки рядом").c();
    });

    public AutoLeave() {
        a(this.b, this.c, this.d);
    }

    @EventTarget
    public void a(TickEvent event) {
        if (mc.world.getRegistryKey().getValue().toString().equals("minecraft:lobby")) {
            return;
        }
        if (ServerUtil.a.a() && ServerUtil.a.d() == -1) {
            return;
        }
        PlayerEntity near = mc.world.getPlayers().stream().filter(player -> {
            return player != mc.player && !(mc.player.squaredDistanceTo(player) > ((double) (this.d.c().floatValue() * this.d.c().floatValue()))) && !Delta.getInstance().getModuleProcessor().e().d(player.getName().getString());
        }).findFirst().orElse(null);
        if (((this.b.a("Малое ХП").c().booleanValue() && mc.player.getHealth() <= this.c.c().floatValue()) || (this.b.a("Игроки рядом").c().booleanValue() && near != null)) && !ServerUtil.e()) {
            mc.player.networkHandler.sendChatCommand("hub");
            if (near != null) {
                ChatUtil.sendMessage("Покинул анархию: рядом игрок &c" + near.getName().getString() + "&7 в &c" + Math.round(Math.sqrt(mc.player.squaredDistanceTo(near))) + "&7 блоках.");
            } else {
                ChatUtil.sendMessage("Покинул &cанархию: критически мало здоровья&7.");
            }
            a();
        }
    }
}
