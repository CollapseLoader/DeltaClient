package aethereal.ui.element;


import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Fonts;
import aethereal.setting.SliderSetting;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector4f;

public class SliderElement extends Element<SliderSetting> {
    private boolean isDragging;

    public SliderElement(SliderSetting setting) {
        super(setting);
        this.a.w = 22.0f;
    }

    @Override

    public boolean onMouseClick(double mouseX, double mouseY, int button) {
        Vector4f vector4f = this.a;
        var setting = this.b;
        if (!MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y + Fonts.c.a(6.5f), vector4f.z, 11.0f)) {
            return false;
        }
        if (button == 0) {
            this.isDragging = true;
            updateSliderFromMouse(mouseX);
            return true;
        }
        if (button != 2) {
            return false;
        }
        if (!(setting instanceof SliderSetting)) {
            throw new ClassCastException();
        }
        setting.b();
        return true;
    }

    @Override

    public boolean onMouseRelease(double mouseX, double mouseY, int button) {
        this.isDragging = false;
        return false;
    }

    @Override

    public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        var setting = this.b;
        Vector4f vector4f = this.a;
        if (!(setting instanceof SliderSetting)) {
            throw new ClassCastException();
        }
        SliderSetting sliderSetting = setting;
        if (!sliderSetting.e || !MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, ((vector4f.y + Fonts.c.a(6.5f)) + 9.5f) - vector4f.y)) {
            return false;
        }
        Float fC = sliderSetting.c();
        if (!(fC instanceof Float)) {
            throw new ClassCastException();
        }
        sliderSetting.a(Float.valueOf(MathUtil.b(Math.round((fC.floatValue() + (((float) Math.signum(amount)) * sliderSetting.c)) / sliderSetting.c) * sliderSetting.c, sliderSetting.a, sliderSetting.b)));
        return true;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        this.a.w = 18.0f;
        if (this.isDragging) {
            updateSliderFromMouse(mouseX);
        }
        getActivationAnimation().c(MathUtil.c(getActivationAnimation().a(), (this.b.c().floatValue() - this.b.a) / (this.b.b - this.b.a), 1.0f));
        float progress = getActivationAnimation().a();
        boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0f;
        float current = this.b.a + ((this.b.b - this.b.a) * progress);
        String value = this.b.c % 1.0f == 0.0f ? String.valueOf(Math.round(current)) : String.valueOf(Math.round(current * 100.0f) / 100.0f);
        float boxWidth = Fonts.c.a(value, 6.25f) + 6.0f;
        float boxHeight = Fonts.c.a(6.25f) + 2.0f;
        float boxX = (this.a.x + this.a.z) - boxWidth;
        drawLabel(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y + 0.5f, Fonts.c.a(6.5f), 6.5f, theme.a(ThemeInfo.TEXT).toIntColor(), (boxX - this.a.x) - 4.0f, hovered, extend, delta);
        draw.a(matrices, boxX, this.a.y, boxWidth, boxHeight, 2.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.03137255f * extend));
        draw.a(matrices, boxX, this.a.y, boxWidth, boxHeight, 2.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).getAlphaFloat() * extend));
        Fonts.c.b(matrices, value, boxX + (boxWidth / 2.0f), (this.a.y + ((boxHeight - Fonts.c.a(6.25f)) / 2.0f)) - 0.5f, 6.25f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT).toIntColor(), extend));
        float trackY = this.a.y + Fonts.c.a(6.5f) + 6.5f;
        draw.a(matrices, this.a.x, trackY, this.a.z, 3.0f, 0.75f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(50, 52, 60, 255), extend * 0.35f));
        draw.a(matrices, this.a.x, trackY, this.a.z * progress, 3.0f, 0.75f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), extend));
        draw.a(matrices, this.a.x + ((this.a.z - 6.0f) * progress), (trackY + 1.5f) - 3.0f, 6.0f, 6.0f, 2.0f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(255, 255, 255, 255), extend));
    }

    private void updateSliderFromMouse(double mouseX) {
        float progress = MathUtil.b(((float) (mouseX - ((double) this.a.x))) / this.a.z, 0.0f, 1.0f);
        float value = this.b.a + ((this.b.b - this.b.a) * progress);
        this.b.a(Float.valueOf(MathUtil.b(Math.round(value / this.b.c) * this.b.c, this.b.a, this.b.b)));
    }
}
