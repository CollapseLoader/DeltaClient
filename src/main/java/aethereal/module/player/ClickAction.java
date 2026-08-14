package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.setting.BindSetting;
import aethereal.util.ChatUtil;
import lombok.Generated;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.EntityHitResult;

@ModuleRegister(a = "Click Action", b = "Выполняет действие, привязанное к выбранной клавише", c = Category.Player)
public class ClickAction extends Module implements Interface {
    private final BindSetting b = new BindSetting("Эндер-жемчуг", -1).a(() -> {
        Delta.h().d().v().b().a(Items.ENDER_PEARL.getDefaultStack());
    });
    private final BindSetting c = new BindSetting("Добавление друга", -1).a(() -> {
        EntityHitResult hit = aM_.crosshairTarget instanceof EntityHitResult ehr ? ehr : null;
        if (hit != null) {
            if (hit.getEntity() instanceof AbstractClientPlayerEntity targetPlayer) {
                if (targetPlayer != aM_.player) {
                    String name = targetPlayer.getName().getString();
                    if (Delta.h().d().e().d(name)) {
                        Delta.h().d().e().c(name);
                        Delta.h().d().e().unSetup();
                        ChatUtil.sendMessage("Товарищ " + name + " был успешно удален из списка друзей.");
                    } else {
                        Delta.h().d().e().b(name);
                        Delta.h().d().e().unSetup();
                        ChatUtil.sendMessage("Товарищ " + name + " был успешно добавлен в список друзей.");
                    }
                }
            }
        }
    });

    public ClickAction() {
        a(this.b, this.c);
    }

    @Generated
    public BindSetting q() {
        return this.b;
    }

    @Generated
    public BindSetting r() {
        return this.c;
    }
}
