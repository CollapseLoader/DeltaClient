package aethereal.ui.widget;

import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.event.BackendEvent;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.Setting;
import aethereal.ui.element.DragInfo;
import aethereal.ui.element.Element;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class Widget {
    protected final AnimationUtil a = new AnimationUtil();
    protected final AnimationUtil b = new AnimationUtil();
    protected final AnimationUtil c = new AnimationUtil();
    protected final float d = 12.5f;
    protected final float e = 7.0f;
    private final List<Setting<?>> f = new ObjectArrayList<>();
    private final List<Element<?>> g = new ObjectArrayList<>();
    private final DragInfo i;
    private boolean h = false;

    public Widget(DragInfo dragInfo) {
        this.i = dragInfo;
        dragInfo.setWidget(this);
    }

    public List<Setting<?>> b() {
        return this.f;
    }

    public List<Element<?>> c() {
        return this.g;
    }

    public AnimationUtil d() {
        return this.a;
    }

    public AnimationUtil e() {
        return this.b;
    }

    public AnimationUtil f() {
        return this.c;
    }

    public void a(boolean status) {
        this.h = status;
    }

    public boolean g() {
        return this.h;
    }

    public float h() {
        return this.d;
    }

    public float i() {
        return this.e;
    }

    public DragInfo j() {
        return this.i;
    }

    protected final void a(Setting<?>... settings) {
        for (Setting<?> setting : settings) {
            this.f.add(setting);
            this.g.add(setting.createBooleanElement());
        }
    }

    public void a(DrawEvent event) {
        e().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        this.c.a(this.h && (Interface.mc.currentScreen instanceof ChatScreen));
        this.c.a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        if (this.c.c() > 0.0f) {
            b(event);
        }
    }

    public void a(GlobalEvent event) {
        e().a(this.i == Delta.getInstance().getModuleProcessor().s().getActiveDragInfo());
    }

    public void a(PacketEvent event) {
    }

    public void a(BackendEvent event) {
    }

    protected void b(DrawEvent event) {
        List<Element<?>> visible = this.g.stream().filter(e -> {
            return e.getSetting().e().get().booleanValue();
        }).toList();
        if (!visible.isEmpty()) {
            float panelWidth = visible.stream().map(e2 -> {
                return Float.valueOf(19.5f + Fonts.e.a(e2.getSetting().i(), 6.5f) + 25.0f);
            }).reduce(Float.valueOf(0.0f), (v0, v1) -> {
                return Math.max(v0, v1);
            }).floatValue();
            float totalHeight = (12.0f * visible.size()) + (visible.size() - 1);
            float anim = this.c.c() * a();
            float baseX = (this.i.getClampedY() - totalHeight) - 2.0f >= 0.0f
                    ? (this.i.getClampedX() + (this.i.getWidth() / 2.0f)) - (panelWidth / 2.0f)
                    : this.i.getClampedX() + this.i.getWidth() + 2.0f;
            float baseY = (this.i.getClampedY() - totalHeight) - 2.0f >= 0.0f ? (this.i.getClampedY() - totalHeight) - 2.0f : this.i.getClampedY();
            float baseX2 = Math.min(Math.max(baseX, 0.0f),
                    (Interface.mc.getWindow().getScaledWidth() - panelWidth) - 2.0f);
            float baseY2 = Math.min(Math.max(baseY, 0.0f), Interface.mc.getWindow().getScaledHeight() - totalHeight);
            a(event, baseX2, baseY2, panelWidth, totalHeight, true, anim);
            float y = baseY2;
            for (Element<?> element : visible) {
                element.getBounds().set(baseX2, y, panelWidth, 12.0f);
                element.onDrawEvent(event, baseX2, y, panelWidth, anim);
                y += 12.0f + 1.0f;
                if (element != visible.getLast()) {
                    event.getDraw2DProcessor().a(event.i().getMatrices(), baseX2, y - 1.0f, panelWidth, 0.75f, 0.0f, ColorUtil.applyAlphaToColor(
                            ColorUtil.convertToARGB(InterfaceC0020Opcode.aN, InterfaceC0020Opcode.aN, InterfaceC0020Opcode.aN, 255),
                            0.2f * anim));
                }
            }
        }
    }

    protected void a(DrawEvent event, String icon, Object title, float width, float animation) {
        a(event, icon, title, width, animation, Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor());
    }

    protected void a(DrawEvent event, String icon, Object title, float width, float animation, int iconColor) {
        a(event, this.i.getClampedX(), this.i.getClampedY(), icon, title, width, animation, iconColor);
    }

    protected void a(DrawEvent event, float x, float y, String icon, Object title, float width, float animation,
                     int iconColor) {
        a(event, x, y, icon, null, title, width, animation, iconColor);
    }

    protected void a(DrawEvent event, float x, float y, ItemStack icon, Object title, float width, float animation,
                     int iconColor) {
        a(event, x, y, null, icon, title, width, animation, iconColor);
    }

    private void a(DrawEvent event, float x, float y, String icon, ItemStack stack, Object title, float width,
                   float animation, int iconColor) {
        if (animation > 0.0f) {
            float iconSize = this.e + 1.0f;
            a(event, x, y, width, this.d, true, animation);
            if (stack != null) {
                Delta.getInstance().getModuleProcessor().j().a(event.i(), stack, x + 3.0f, (y + ((this.d - 8.0f) / 2.0f)) - 0.25f, 0,
                        animation, 0.5f, false);
            } else {
                Fonts.a.a(event.h(), icon, x + 3.0f, y + ((this.d - Fonts.a.a(iconSize)) / 2.0f), iconSize,
                        ColorUtil.applyAlphaToColor(iconColor, animation));
            }
            a(event, x + 13.5f, y, this.d, animation);
            if (title instanceof Text text) {
                Fonts.e.a(event.h(), text, x + 17.5f, (y + ((this.d - Fonts.e.a(this.e)) / 2.0f)) - 0.5f, this.e,
                        animation);
            } else {
                Fonts.e.a(event.h(), String.valueOf(title), x + 17.5f,
                        (y + ((this.d - Fonts.e.a(this.e)) / 2.0f)) - 0.5f, this.e, ColorUtil.applyAlphaToColor(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.TEXT).toIntColor(), animation));
            }
        }
    }

    protected void a(DrawEvent event, float x, float y, float width, float height, boolean glow, float animation) {
        if (animation > 0.0f) {
            ThemeProcessor themeProcessor = Delta.getInstance().getModuleProcessor().o();
            float alpha = themeProcessor.a(ThemeInfo.BACKGROUND_HUD).getAlphaFloat() * animation;
            int background = ColorUtil.lerpColor(themeProcessor.a(ThemeInfo.BACKGROUND_HUD).toIntColor(),
                    themeProcessor.a(ThemeInfo.PRIMARY).toIntColor(), themeProcessor.a(ThemeInfo.PRIMARY).getAlphaFloat() / 6.0f);
            themeProcessor.a(ThemeInfo.BACKGROUND_HUD).setAlpha(InterfaceC0020Opcode.cY);
            if (glow) {
                event.getDraw2DProcessor().a(event.h(), x, y, width, height, 5.0f + (this.b.c()), ColorUtil.applyAlphaToColor(background, alpha),
                        animation, ColorUtil.applyAlphaToColor(background, alpha), 8.0f + (2.0f * this.b.c()));
            } else {
                event.getDraw2DProcessor().b(event.h(), x, y, width, height, 5.0f, ColorUtil.applyAlphaToColor(background, alpha), animation);
            }
        }
    }

    protected void a(DrawEvent event, float x, float y, float height, float animation) {
        float separatorHeight = height / 2.0f;
        event.getDraw2DProcessor().a(event.i().getMatrices(), x, y + ((height - separatorHeight) / 2.0f), 0.75f, separatorHeight, 0.0f,
                ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(InterfaceC0020Opcode.aN, InterfaceC0020Opcode.aN, InterfaceC0020Opcode.aN, 255),
                        0.5f * animation));
    }

    public float a() {
        return this.a.c() * (1.0f - (0.1f * this.b.c()));
    }
}
