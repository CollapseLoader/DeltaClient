package aethereal.ui.element;


import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.ColorSetting;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Vector4f;

import java.awt.*;

public class ColorElement extends Element_2<ColorSetting> {
    private final Vector4f d;
    private final Vector4f e;
    private final Vector4f f;
    private final Vector4f g;
    private float h;
    private float i;
    private float j;
    private float k;
    private DragMode l;
    private boolean m;

    public ColorElement(ColorSetting setting) {
        super(setting);
        this.d = new Vector4f();
        this.e = new Vector4f();
        this.f = new Vector4f();
        this.g = new Vector4f();
        this.l = DragMode.NONE;
        this.a.w = 11.0f;
        g();
    }

    @Override

    public boolean a(double mouseX, double mouseY, int button) {
        Vector4f vector4f = this.a;
        Vector4f vector4f2 = this.e;
        Vector4f vector4f3 = this.f;
        Vector4f vector4f4 = this.g;
        if (MathUtil.a(mouseX, mouseY, (vector4f.x + vector4f.z) - 11.0f, (vector4f.y + (vector4f.w / 2.0f)) - 5.0f, 11.0f, 11.0f)) {
            this.m = !this.m;
            return true;
        }
        if (!this.m || button != 0) {
            return false;
        }
        if (MathUtil.a(mouseX, mouseY, vector4f2.x, vector4f2.y, vector4f2.z, vector4f2.w)) {
            this.l = DragMode.AREA;
            a(mouseX, mouseY);
            return true;
        }
        if (MathUtil.a(mouseX, mouseY, vector4f3.x, vector4f3.y, vector4f3.z, vector4f3.w)) {
            this.l = DragMode.HUE;
            a(mouseX, mouseY);
            return true;
        }
        if (!MathUtil.a(mouseX, mouseY, vector4f4.x, vector4f4.y, vector4f4.z, vector4f4.w)) {
            return false;
        }
        this.l = DragMode.ALPHA;
        a(mouseX, mouseY);
        return true;
    }

    @Override

    public boolean b(double mouseX, double mouseY, int button) {
        this.l = DragMode.NONE;
        return false;
    }

    @Override
    public void a(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        float centerY = this.a.y + (this.a.w / 2.0f) + 0.5f;
        float boxX = (this.a.x + this.a.z) - 11.0f;
        float boxY = centerY - 5.5f;
        this.e.set(this.a.x + this.a.z + 6.0f + 5.0f, boxY, 56.0f, 56.0f);
        this.f.set(this.e.x + 56.0f + 5.0f, this.e.y, 4.0f, 56.0f);
        this.g.set(this.f.x + 4.0f + 5.0f, this.e.y, 4.0f, 56.0f);
        this.d.set(this.e.x - 5.0f, this.e.y - 5.0f, 84.0f, 66.0f);
        boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0f;
        if (extend < 1.0f) {
            this.m = false;
        }
        a(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y, this.a.w, 6.5f, theme.a(ThemeInfo.TEXT).toIntColor(), (boxX - this.a.x) - 4.0f, hovered, extend, delta);
        draw.a(matrices, boxX, boxY, 11.0f, 11.0f, 2.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.039215688f * extend));
        draw.a(matrices, boxX, boxY, 11.0f, 11.0f, 2.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b() * extend));
        Fonts.a.a(matrices, "J", boxX + ((11.0f - Fonts.a.b("J", 6.5f)) / 2.0f), Fonts.a.a("J", 6.5f, centerY), 6.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), extend));
        draw.a(matrices, ((boxX + 11.0f) - 3.0f) - 1.25f, ((boxY + 11.0f) - 3.0f) - 1.25f, 3.0f, 3.0f, 0.5f, ColorUtil.applyAlphaToColor(this.b.c().intValue(), extend));
    }

    @Override
    public void a(DrawContext context, double mouseX, double mouseY, float delta) {
        b().a(this.m);
        b().a(0.0f, 1.0f, 0.25f, EasingList.p, delta);
        float anim = EasingList.p.ease(b().c());
        if (anim > 0.0f) {
            MatrixStack matrices = context.getMatrices();
            Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
            ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
            a(mouseX, mouseY);
            int hueColor = Color.HSBtoRGB(this.h, 1.0f, 1.0f);
            int rgb = this.b.c().intValue() & 16777215;
            int handle = ColorUtil.applyAlphaToColor(16777215, anim);
            int background = ColorUtil.applyAlphaToColor(ColorUtil.lerpColor(theme.a(ThemeInfo.BACKGROUND_GUI).toIntColor(), theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.05f), 0.8235294f * anim);
            float scale = 0.85f + (0.15f * EasingList.s.ease(b().c()));
            float centerX = this.d.x + (this.d.z / 2.0f);
            float centerY = this.d.y + (this.d.w / 2.0f);
            matrices.push();
            matrices.translate(centerX, centerY + ((1.0f - anim) * 6.0f), 0.0f);
            matrices.scale(scale, scale, 1.0f);
            matrices.translate(-centerX, -centerY, 0.0f);
            draw.b(matrices, this.d.x, this.d.y, this.d.z, this.d.w, 4.0f, background, anim);
            draw.a(matrices, this.d.x, this.d.y, this.d.z, this.d.w, 4.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b() * anim));
            draw.a(matrices, this.e.x, this.e.y, this.e.z, this.e.w, 2.0f, ColorUtil.applyAlphaToColor(16777215, anim), ColorUtil.applyAlphaToColor(hueColor, anim), ColorUtil.applyAlphaToColor(0, anim), ColorUtil.applyAlphaToColor(0, anim));
            draw.a(matrices, this.e.x, this.e.y, this.e.z, this.e.w, 2.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).b() * anim));
            float cursorX = MathUtil.b(this.e.x + (this.i * this.e.z), this.e.x + 2.0f, (this.e.x + this.e.z) - 2.0f);
            float cursorY = MathUtil.b(this.e.y + ((1.0f - this.j) * this.e.w), this.e.y + 2.0f, (this.e.y + this.e.w) - 2.0f);
            draw.a(matrices, cursorX - 2.0f, cursorY - 2.0f, 4.0f, 4.0f, 1.0f, 0.5f, handle);
            float knob = this.f.z + 2.0f;
            draw.a(matrices, Identifier.of("delta", "pictures/color.png"), this.f.x, this.f.y, this.f.z, this.f.w, this.f.z / 4.0f, ColorUtil.applyAlphaToColor(16777215, anim));
            draw.a(context, this.f.x - 1.0f, (this.f.y + (this.h * this.f.w)) - 0.5f, knob, 1.0f, handle);
            draw.a(matrices, Identifier.of("delta", "pictures/opacity.png"), this.g.x, this.g.y, this.g.z, this.g.w, this.g.z / 4.0f, ColorUtil.applyAlphaToColor(16777215, 0.019607844f * anim));
            draw.a(matrices, this.g.x, this.g.y, this.g.z, this.g.w, this.g.z / 4.0f, ColorUtil.applyAlphaToColor(rgb, anim), ColorUtil.applyAlphaToColor(rgb, anim), ColorUtil.applyAlphaToColor(rgb, 0.0f), ColorUtil.applyAlphaToColor(rgb, 0.0f));
            draw.a(context, this.g.x - 1.0f, (this.g.y + ((1.0f - this.k) * this.g.w)) - 0.5f, knob, 1.0f, handle);
            matrices.pop();
        }
    }

    private void a(double mouseX, double mouseY) {
        switch (this.l) {
            case DragMode.NONE:
                return;
            case DragMode.AREA:
                this.i = MathUtil.b(((float) (mouseX - ((double) this.e.x))) / this.e.z, 0.0f, 1.0f);
                this.j = 1.0f - MathUtil.b(((float) (mouseY - ((double) this.e.y))) / this.e.w, 0.0f, 1.0f);
                break;
            case DragMode.HUE:
                this.h = MathUtil.b(((float) (mouseY - ((double) this.f.y))) / this.f.w, 0.0f, 1.0f);
                break;
            case DragMode.ALPHA:
                this.k = 1.0f - MathUtil.b(((float) (mouseY - ((double) this.g.y))) / this.g.w, 0.0f, 1.0f);
                break;
        }
        this.b.a(Integer.valueOf(ColorUtil.applyAlphaToColor(Color.HSBtoRGB(this.h, this.i, this.j), this.k)));
    }

    private void g() {
        int color = this.b.c().intValue();
        float[] hsb = Color.RGBtoHSB((color >> 16) & 255, (color >> 8) & 255, color & 255, null);
        this.h = hsb[0];
        this.i = hsb[1];
        this.j = hsb[2];
        this.k = ((color >> 24) & 255) / 255.0f;
    }

    enum DragMode {
        NONE,
        AREA,
        HUE,
        ALPHA
    }
}
