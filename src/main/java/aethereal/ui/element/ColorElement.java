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

public class ColorElement extends Element<ColorSetting> {
    private final Vector4f pickerBackground;
    private final Vector4f satArea;
    private final Vector4f hueBar;
    private final Vector4f g;
    private float h;
    private float i;
    private float j;
    private float k;
    private DragMode l;
    private boolean m;

    public ColorElement(ColorSetting setting) {
        super(setting);
        this.pickerBackground = new Vector4f();
        this.satArea = new Vector4f();
        this.hueBar = new Vector4f();
        this.g = new Vector4f();
        this.l = DragMode.NONE;
        this.a.w = 11.0f;
        initFromSetting();
    }

    @Override

    public boolean onMouseClick(double mouseX, double mouseY, int button) {
        Vector4f vector4f = this.a;
        Vector4f vector4f2 = this.satArea;
        Vector4f vector4f3 = this.hueBar;
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
            updateColorFromMouse(mouseX, mouseY);
            return true;
        }
        if (MathUtil.a(mouseX, mouseY, vector4f3.x, vector4f3.y, vector4f3.z, vector4f3.w)) {
            this.l = DragMode.HUE;
            updateColorFromMouse(mouseX, mouseY);
            return true;
        }
        if (!MathUtil.a(mouseX, mouseY, vector4f4.x, vector4f4.y, vector4f4.z, vector4f4.w)) {
            return false;
        }
        this.l = DragMode.ALPHA;
        updateColorFromMouse(mouseX, mouseY);
        return true;
    }

    @Override

    public boolean onMouseRelease(double mouseX, double mouseY, int button) {
        this.l = DragMode.NONE;
        return false;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        float centerY = this.a.y + (this.a.w / 2.0f) + 0.5f;
        float boxX = (this.a.x + this.a.z) - 11.0f;
        float boxY = centerY - 5.5f;
        this.satArea.set(this.a.x + this.a.z + 6.0f + 5.0f, boxY, 56.0f, 56.0f);
        this.hueBar.set(this.satArea.x + 56.0f + 5.0f, this.satArea.y, 4.0f, 56.0f);
        this.g.set(this.hueBar.x + 4.0f + 5.0f, this.satArea.y, 4.0f, 56.0f);
        this.pickerBackground.set(this.satArea.x - 5.0f, this.satArea.y - 5.0f, 84.0f, 66.0f);
        boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0f;
        if (extend < 1.0f) {
            this.m = false;
        }
        drawLabel(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y, this.a.w, 6.5f, theme.a(ThemeInfo.TEXT).toIntColor(), (boxX - this.a.x) - 4.0f, hovered, extend, delta);
        draw.a(matrices, boxX, boxY, 11.0f, 11.0f, 2.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.039215688f * extend));
        draw.a(matrices, boxX, boxY, 11.0f, 11.0f, 2.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).getAlphaFloat() * extend));
        Fonts.a.a(matrices, "J", boxX + ((11.0f - Fonts.a.b("J", 6.5f)) / 2.0f), Fonts.a.a("J", 6.5f, centerY), 6.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), extend));
        draw.a(matrices, ((boxX + 11.0f) - 3.0f) - 1.25f, ((boxY + 11.0f) - 3.0f) - 1.25f, 3.0f, 3.0f, 0.5f, ColorUtil.applyAlphaToColor(this.b.c().intValue(), extend));
    }

    @Override
    public void renderColorPicker(DrawContext context, double mouseX, double mouseY, float delta) {
        getActivationAnimation().a(this.m);
        getActivationAnimation().a(0.0f, 1.0f, 0.25f, EasingList.p, delta);
        float anim = EasingList.p.ease(getActivationAnimation().c());
        if (anim > 0.0f) {
            MatrixStack matrices = context.getMatrices();
            Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
            ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
            updateColorFromMouse(mouseX, mouseY);
            int hueColor = Color.HSBtoRGB(this.h, 1.0f, 1.0f);
            int rgb = this.b.c().intValue() & 16777215;
            int handle = ColorUtil.applyAlphaToColor(16777215, anim);
            int background = ColorUtil.applyAlphaToColor(ColorUtil.lerpColor(theme.a(ThemeInfo.BACKGROUND_GUI).toIntColor(), theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.05f), 0.8235294f * anim);
            float scale = 0.85f + (0.15f * EasingList.s.ease(getActivationAnimation().c()));
            float centerX = this.pickerBackground.x + (this.pickerBackground.z / 2.0f);
            float centerY = this.pickerBackground.y + (this.pickerBackground.w / 2.0f);
            matrices.push();
            matrices.translate(centerX, centerY + ((1.0f - anim) * 6.0f), 0.0f);
            matrices.scale(scale, scale, 1.0f);
            matrices.translate(-centerX, -centerY, 0.0f);
            draw.b(matrices, this.pickerBackground.x, this.pickerBackground.y, this.pickerBackground.z, this.pickerBackground.w, 4.0f, background, anim);
            draw.a(matrices, this.pickerBackground.x, this.pickerBackground.y, this.pickerBackground.z, this.pickerBackground.w, 4.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).getAlphaFloat() * anim));
            draw.a(matrices, this.satArea.x, this.satArea.y, this.satArea.z, this.satArea.w, 2.0f, ColorUtil.applyAlphaToColor(16777215, anim), ColorUtil.applyAlphaToColor(hueColor, anim), ColorUtil.applyAlphaToColor(0, anim), ColorUtil.applyAlphaToColor(0, anim));
            draw.a(matrices, this.satArea.x, this.satArea.y, this.satArea.z, this.satArea.w, 2.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).getAlphaFloat() * anim));
            float cursorX = MathUtil.b(this.satArea.x + (this.i * this.satArea.z), this.satArea.x + 2.0f, (this.satArea.x + this.satArea.z) - 2.0f);
            float cursorY = MathUtil.b(this.satArea.y + ((1.0f - this.j) * this.satArea.w), this.satArea.y + 2.0f, (this.satArea.y + this.satArea.w) - 2.0f);
            draw.a(matrices, cursorX - 2.0f, cursorY - 2.0f, 4.0f, 4.0f, 1.0f, 0.5f, handle);
            float knob = this.hueBar.z + 2.0f;
            draw.a(matrices, Identifier.of("delta", "pictures/color.png"), this.hueBar.x, this.hueBar.y, this.hueBar.z, this.hueBar.w, this.hueBar.z / 4.0f, ColorUtil.applyAlphaToColor(16777215, anim));
            draw.a(context, this.hueBar.x - 1.0f, (this.hueBar.y + (this.h * this.hueBar.w)) - 0.5f, knob, 1.0f, handle);
            draw.a(matrices, Identifier.of("delta", "pictures/opacity.png"), this.g.x, this.g.y, this.g.z, this.g.w, this.g.z / 4.0f, ColorUtil.applyAlphaToColor(16777215, 0.019607844f * anim));
            draw.a(matrices, this.g.x, this.g.y, this.g.z, this.g.w, this.g.z / 4.0f, ColorUtil.applyAlphaToColor(rgb, anim), ColorUtil.applyAlphaToColor(rgb, anim), ColorUtil.applyAlphaToColor(rgb, 0.0f), ColorUtil.applyAlphaToColor(rgb, 0.0f));
            draw.a(context, this.g.x - 1.0f, (this.g.y + ((1.0f - this.k) * this.g.w)) - 0.5f, knob, 1.0f, handle);
            matrices.pop();
        }
    }

    private void updateColorFromMouse(double mouseX, double mouseY) {
        switch (this.l) {
            case DragMode.NONE:
                return;
            case DragMode.AREA:
                this.i = MathUtil.b(((float) (mouseX - ((double) this.satArea.x))) / this.satArea.z, 0.0f, 1.0f);
                this.j = 1.0f - MathUtil.b(((float) (mouseY - ((double) this.satArea.y))) / this.satArea.w, 0.0f, 1.0f);
                break;
            case DragMode.HUE:
                this.h = MathUtil.b(((float) (mouseY - ((double) this.hueBar.y))) / this.hueBar.w, 0.0f, 1.0f);
                break;
            case DragMode.ALPHA:
                this.k = 1.0f - MathUtil.b(((float) (mouseY - ((double) this.g.y))) / this.g.w, 0.0f, 1.0f);
                break;
        }
        this.b.a(Integer.valueOf(ColorUtil.applyAlphaToColor(Color.HSBtoRGB(this.h, this.i, this.j), this.k)));
    }

    private void initFromSetting() {
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
