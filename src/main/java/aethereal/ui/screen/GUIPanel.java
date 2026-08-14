package aethereal.ui.screen;


import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Category;
import aethereal.core.Delta;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.core.Module;
import aethereal.render.*;
import aethereal.ui.element.Element;
import aethereal.util.KeyUtil;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector4f;

import java.util.Iterator;
import java.util.List;

public class GUIPanel {
    private final Vector4f a = new Vector4f(0.0f, 0.0f, 125.0f, 270.0f);
    private final AnimationUtil b = new AnimationUtil();
    private final AnimationUtil c = new AnimationUtil();
    private final Category d;
    private List<Module> e;
    private Module f;

    public GUIPanel(Category category) {
        this.d = category;
    }


    public boolean a(final double mouseX, final double mouseY, final int button) {
        for (Module module : this.e) {
            if (module.n()) {
                module.a(-100 + button);
                module.b(false);
                return true;
            }
        }
        if (this.f != null) {
            if (button == 0) {
                this.f.a();
                return true;
            }
            if (button == 1) {
                this.f.c(!this.f.o());
                return true;
            }
            if (button == 2) {
                this.e.forEach(obj -> this.i(obj));
                return true;
            }
        }
        return this.e.stream().filter(Module::o).flatMap(module -> module.d().stream()).filter(Element::isEnabled).anyMatch(element -> element.onMouseClick(mouseX, mouseY, button));
    }


    public boolean b(final double mouseX, final double mouseY, final int button) {
        return this.e.stream().filter(Module::o).flatMap(module -> module.d().stream()).filter(Element::isEnabled).anyMatch(element -> element.onMouseRelease(mouseX, mouseY, button));
    }


    public boolean a(final double mouseX, final double mouseY, final int button, final double deltaX, final double deltaY) {
        return this.e.stream().filter(Module::o).flatMap(module -> module.d().stream()).filter(Element::isEnabled).anyMatch(element -> element.onMouseDrag(mouseX, mouseY, button, deltaX, deltaY));
    }


    public boolean a(final int keyCode, final int scanCode, final int modifiers) {
        for (Module module : this.e) {
            if (module.n()) {
                module.a(keyCode);
                module.b(false);
                return true;
            }
        }
        return this.e.stream().filter(Module::o).flatMap(module -> module.d().stream()).filter(Element::isEnabled).anyMatch(element -> element.onKeyPress(keyCode, scanCode, modifiers));
    }


    public boolean a(final char chr, final int modifiers) {
        return this.e.stream().filter(Module::o).flatMap(module -> module.d().stream()).filter(Element::isEnabled).anyMatch(element -> element.onCharTyped(chr, modifiers));
    }


    public boolean a(final double mouseX, final double mouseY, final double amount) {
        if (!MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w)) {
            return false;
        }
        if (this.e.stream().filter(Module::o).flatMap(module -> module.d().stream()).filter(Element::isEnabled).anyMatch(element -> element.onMouseScroll(mouseX, mouseY, amount))) {
            return true;
        }
        this.b.a((float) amount * 15.0f);
        return true;
    }

    public void a(List<Module> modules) {
        this.e = modules;
    }

    public void a(Module hovered) {
        this.f = hovered;
    }

    public Vector4f f() {
        return this.a;
    }

    public AnimationUtil a() {
        return this.b;
    }

    public AnimationUtil b() {
        return this.c;
    }

    public Category c() {
        return this.d;
    }

    public List<Module> d() {
        return this.e;
    }

    public Module e() {
        return this.f;
    }

    public void a(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        float scale = 0.8f + (0.2f * EasingList.s.ease(this.c.c()));
        matrices.push();
        matrices.translate(this.a.x + (this.a.z / 2.0f), this.a.y + (this.a.w / 2.0f) + ((1.0f - EasingList.p.ease(this.c.c())) * 14.0f), 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate(-(this.a.x + (this.a.z / 2.0f)), -(this.a.y + (this.a.w / 2.0f)), 0.0f);
        int background = ColorUtil.combineColorWithAlpha(ColorUtil.lerpColor(theme.a(ThemeInfo.BACKGROUND_GUI).toIntColor(), theme.a(ThemeInfo.PRIMARY).toIntColor(), theme.a(ThemeInfo.PRIMARY).getAlphaFloat() / 4.0f), InterfaceC0020Opcode.aN);
        draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 8.0f, background, 1.0f, background, 16.0f);
        draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 8.0f, 0.5f, theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor());
        a(matrices, theme, 24.0f);
        a(context, mouseX, mouseY, this.a.y + 24.0f + 4.0f, delta);
        matrices.pop();
    }

    private void a(MatrixStack matrices, ThemeProcessor theme, float header) {
        float headerCenter = this.a.y + (header / 2.0f);
        float titleX = this.a.x + 6.0f + 4.0f;
        float iconWidth = Fonts.a.b(this.d.a(), 9.0f);
        float iconX = (((this.a.x + this.a.z) - 6.0f) - 4.0f) - iconWidth;
        int color = ColorUtil.lerpColor(ColorUtil.convertToARGB(255, 255, 255, 255), ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 1.0f), 0.25f);
        Fonts.c.a(matrices, this.d.name(), titleX, Fonts.c.a(this.d.name(), 9.0f, headerCenter), 9.0f, color);
        Fonts.a.a(matrices, this.d.a(), iconX, Fonts.a.a(this.d.a(), 9.0f, headerCenter), 9.0f, color);
    }

    private void a(DrawContext context, int mouseX, int mouseY, float y, float delta) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        float view = (((this.a.y + this.a.w) - 6.0f) - y) + 4.0f;
        float content = 0.0f;
        Iterator<Module> it = this.e.iterator();
        while (it.hasNext()) {
            content += b(it.next()) + 4.0f;
        }
        float y2 = y + this.b.a(Math.min(0.0f, view - content), 0.0f, 1.0f);
        this.f = null;
        ScissorUtil.a(matrices, this.a.x, y, this.a.z, view);
        float bottom = y + view;
        Iterator<Module> it2 = this.e.iterator();
        while (it2.hasNext()) {
            Module module = it2.next();
            float center = y2 + 8.0f;
            float activation = module.f().c();
            float fade = (float) Math.pow(MathUtil.a(MathUtil.b((bottom - y2) / 16.0f, 0.0f, 1.0f)), 1.0d);
            boolean hover = ((float) mouseY) >= y && ((float) mouseY) <= bottom && MathUtil.a(mouseX, mouseY, this.a.x + 6.0f, y2, this.a.z - 12.0f, 16.0f);
            if (hover) {
                this.f = module;
            }
            module.h().a(0.0f, 1.0f, 0.5f, EasingList.i, delta);
            module.i().a(0.0f, 1.0f, 0.25f, EasingList.i, delta);
            module.h().a(module.o());
            module.i().a(module == this.f);
            float total = b(module);
            if (y2 + total > y && y2 < bottom) {
                draw.a(matrices, this.a.x + 6.0f, y2, this.a.z - 12.0f, total, 4.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.039215688f * activation * fade));
                draw.a(matrices, this.a.x + 6.0f, y2, this.a.z - 12.0f, total, 4.0f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(255, 255, 255, 255), 0.023529412f * module.i().c() * fade));
                draw.a(matrices, this.a.x + 6.0f, y2, this.a.z - 12.0f, total, 4.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).getAlphaFloat() * activation * fade));
                Fonts.c.a(matrices, module.j(), this.a.x + 6.0f + 4.0f, (center - (Fonts.c.a(7.25f) / 2.0f)) - 0.5f, 7.25f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT).toIntColor(), fade));
                if (module.g().c() > 0.0f) {
                    float bind = module.g().c();
                    String bindText = module.n() ? "?" : KeyUtil.b(module.p());
                    float iconWidth = Fonts.a.b("C", 6.0f);
                    float boxWidth = 4.0f + iconWidth + 2.5f + Fonts.c.a(bindText, 6.0f) + 4.0f;
                    float boxX = this.a.x + 6.0f + 4.0f + Fonts.c.a(module.j(), 7.25f) + 4.0f;
                    float boxY = center - 4.5f;
                    draw.a(matrices, boxX, boxY, boxWidth, 9.0f, 2.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.15686275f * bind));
                    draw.a(matrices, boxX, boxY, boxWidth, 9.0f, 2.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).getAlphaFloat() * bind));
                    Fonts.a.a(matrices, "C", boxX + 4.0f, Fonts.a.a("C", 6.0f, center), 6.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT).toIntColor(), bind));
                    Fonts.c.a(matrices, bindText, boxX + 4.0f + iconWidth + 2.5f, Fonts.c.a(bindText, 6.0f, center), 6.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT).toIntColor(), bind));
                }
                if (module.d().stream().anyMatch((v0) -> {
                    return v0.isEnabled();
                })) {
                    Fonts.c.a(matrices, "...", ((((this.a.x + this.a.z) - 6.0f) - 4.0f) - Fonts.c.a("...", 10.0f)) - (activation > 0.0f ? 18.0f : 0.0f), Fonts.c.a("...", 10.0f, center), 10.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), fade));
                }
                if (activation > 0.0f) {
                    float toggleX = (((this.a.x + this.a.z) - 6.0f) - 4.0f) - 14.0f;
                    float toggleY = center - 4.25f;
                    draw.a(matrices, toggleX, toggleY, 14.0f, 8.5f, 3.25f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.49019608f * activation * fade));
                    draw.a(matrices, toggleX, toggleY, 14.0f, 8.5f, 3.25f, 0.3f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).getAlphaFloat() * activation * fade));
                    draw.a(matrices, toggleX + 1.5f + (5.5f * activation), toggleY + 1.5f, 5.5f, 5.5f, 1.75f, ColorUtil.applyAlphaToColor(ColorUtil.lerpColor(ColorUtil.convertToARGB(InterfaceC0020Opcode.ap, InterfaceC0020Opcode.ap, InterfaceC0020Opcode.bk, 255), ColorUtil.convertToARGB(255, 255, 255, 255), activation), activation * fade));
                }
                float extend = module.h().c();
                if (extend > 0.0f) {
                    ScissorUtil.a(matrices, this.a.x + 6.0f, y2, this.a.z - 12.0f, total);
                    float baseY = (y2 + 16.0f) - (4.0f * (1.0f - extend));
                    float offset = 0.0f;
                    for (Element<?> element : module.d()) {
                        element.getVisibilityAnimation().a(element.isEnabled());
                        element.getVisibilityAnimation().a(0.0f, 1.0f, 0.4f, EasingList.i, delta);
                        float visible = element.getVisibilityAnimation().c();
                        if (visible > 0.0f) {
                            float targetY = (baseY + offset) - (4.0f * (1.0f - visible));
                            float currentY = baseY + ((targetY - baseY) * extend);
                            element.getBounds().set(this.a.x + 6.0f + 4.5f, currentY, (this.a.z - 12.0f) - 8.0f, element.getBounds().w());
                            element.render(context, mouseX, mouseY, delta, extend * visible);
                            offset += (element.getBounds().w() + 4.0f) * visible;
                        }
                    }
                    ScissorUtil.a(matrices);
                }
            }
            y2 += total + 4.0f;
        }
        ScissorUtil.a(matrices);
    }

    public void a(DrawContext context, double mouseX, double mouseY, float delta) {
        for (Module module : this.e) {
            for (Element<?> element : module.d()) {
                element.renderColorPicker(context, mouseX, mouseY, delta);
            }
        }
    }

    private float b(Module module) {
        return 16.0f + (module.d().isEmpty() ? 0.0f : ((float) module.d().stream().mapToDouble(e -> {
            return (e.getBounds().w() + 4.0f) * e.getVisibilityAnimation().c();
        }).sum()) * module.h().c());
    }

    public void i(Module module) {
        module.b(module == this.f && !module.n());
    }
}
