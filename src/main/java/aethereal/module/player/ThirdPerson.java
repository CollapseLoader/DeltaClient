package aethereal.module.player;

import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.KeyEvent;
import aethereal.event.TickEvent;
import aethereal.lib.javassist.TokenId;
import aethereal.setting.BindSetting;
import aethereal.setting.ModeSetting;
import aethereal.util.Look;
import aethereal.util.MathUtil;
import aethereal.util.Rotation;
import net.minecraft.client.option.Perspective;

@ModuleRegister(name = "Third Person", description = "Свободный обзор от третьего лица без изменения направления движения", category = Category.Player)
public class ThirdPerson extends Module {
    private final ModeSetting b = new ModeSetting("Режим активации осмотра", "По нажатию", "По нажатию", "По зажатию");
    private boolean c;
    private Rotation e;

    public ThirdPerson() {
        BindSetting d = new BindSetting("Кнопка осмотра", Integer.valueOf(TokenId.Q_), 0).a(() -> {
            if (this.b.l("По зажатию")) {
                d(true);
            } else {
                d(!this.c);
            }
        }).b(() -> {
            if (this.c && this.b.l("По зажатию")) {
                d(false);
            }
        });
        a(this.b, d);
    }

    @EventTarget
    public void a(KeyEvent event) {
        if (this.c && event.getKey() == mc.options.togglePerspectiveKey.getDefaultKey().getCode()) {
            event.a(true);
        }
    }

    @EventTarget
    public void a(TickEvent event) {
        if (this.c) {
            if (mc.currentScreen != null) {
                d(false);
            } else {
                Delta.getInstance().getModuleProcessor().k().a(
                        new Rotation(mc.player.getYaw(), MathUtil.b(mc.player.getPitch(), -89.0f, 89.0f)), 360.0f, 0,
                        1);
            }
        }
    }

    private void d(boolean active) {
        if (active) {
            this.e = new Rotation(Look.b(), Look.c());
        } else {
            Look.a(this.e.c());
            Look.b(this.e.d());
        }
        mc.options.setPerspective(active ? Perspective.THIRD_PERSON_BACK : Perspective.FIRST_PERSON);
        this.c = active;
    }
}
