package aethereal.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.option.Perspective;

@ModuleRegister(name = "Animations", description = "Анимирует выбранные элементы игры", category = Category.Render)
public class Animations extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Выберите что анимировать", new BooleanSetting("TAB", true), new BooleanSetting("Открытие инвентаря", true), new BooleanSetting("Смена перспективы", true), new BooleanSetting("Поднятие хотбара", true), new BooleanSetting("Слот хотбара", true), new BooleanSetting("Появление сообщений", true), new BooleanSetting("Предметы", true));
    private final AnimationUtil c = new AnimationUtil();
    private final AnimationUtil d = new AnimationUtil();
    private final AnimationUtil e = new AnimationUtil();
    private final AnimationUtil f = new AnimationUtil();
    private float g = -1.0f;

    public Animations() {
        a(this.b);
    }

    public MultiModeSetting q() {
        return this.b;
    }

    public AnimationUtil r() {
        return this.c;
    }

    public AnimationUtil s() {
        return this.d;
    }

    public AnimationUtil t() {
        return this.e;
    }

    public AnimationUtil u() {
        return this.f;
    }

    public float v() {
        return this.g;
    }

    @Override
    public void c() {
        super.c();
        this.g = -1.0f;
    }

    @EventTarget
    public void a(DrawEvent event) {
        this.c.a(0.0f, 1.0f, 0.5f, EasingList.g, event.g());
        this.d.a(0.0f, 1.0f, 0.45f, EasingList.g, event.g());
        this.e.a(0.0f, 1.0f, 0.4f, EasingList.g, event.g());
        this.f.a(0.0f, 1.0f, 0.35f, EasingList.g, event.g());
        this.g = this.g < 0.0f ? mc.player.getInventory().selectedSlot : MathUtil.c(this.g, mc.player.getInventory().selectedSlot, 1.25f);
    }

    @EventTarget
    public void a(TickEvent event) {
        this.e.a(mc.currentScreen instanceof InventoryScreen);
        this.f.a(mc.options.getPerspective() != Perspective.FIRST_PERSON);
        this.d.a(mc.currentScreen instanceof ChatScreen);
    }
}
