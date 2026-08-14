package aethereal.ui.element;


import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.render.*;
import aethereal.setting.BindSetting;
import aethereal.util.KeyUtil;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector4f;

public class BindElement extends Element<BindSetting> {
    private boolean isListening;

    public BindElement(BindSetting setting) {
        super(setting);
        this.a.w = 11.0f;
    }

    @Override

    public boolean onMouseClick(double mouseX, double mouseY, int button) {
        Vector4f vector4f = this.a;
        var setting = this.b;
        if (this.isListening) {
            if (!(setting instanceof BindSetting)) {
                throw new ClassCastException();
            }
            setting.a(Integer.valueOf(-100 + button));
            this.isListening = false;
            return true;
        }
        if (!MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.w)) {
            return false;
        }
        if (button == 0) {
            this.isListening = true;
            return true;
        }
        if (button != 2) {
            return false;
        }
        if (!(setting instanceof BindSetting)) {
            throw new ClassCastException();
        }
        setting.b();
        return true;
    }

    @Override

    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        if (!this.isListening) {
            return false;
        }
        var setting = this.b;
        if (!(setting instanceof BindSetting)) {
            throw new ClassCastException();
        }
        setting.a(Integer.valueOf(keyCode));
        this.isListening = false;
        return true;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        getActivationAnimation().a(this.isListening);
        getActivationAnimation().a(0.0f, 1.0f, 0.4f, EasingList.p, delta);
        float centerY = this.a.y + (this.a.w / 2.0f) + 0.5f;
        boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0f;
        float anim = getActivationAnimation().c();
        float reverse = 1.0f - anim;
        String value = this.b.c().intValue() == -1 ? "None" : KeyUtil.b(this.b.c().intValue());
        float total = (Fonts.c.a(value, 6.5f) * reverse) + (Fonts.c.a("...", 6.5f) * anim);
        float boxWidth = total + 8.0f;
        float boxHeight = Fonts.c.a(6.5f) + 3.0f;
        float boxX = (this.a.x + this.a.z) - boxWidth;
        float boxY = centerY - (boxHeight / 2.0f);
        float textY = (boxY + ((boxHeight - Fonts.c.a(6.5f)) / 2.0f)) - 0.75f;
        drawLabel(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y, this.a.w, 6.5f, theme.a(ThemeInfo.TEXT).toIntColor(), (boxX - this.a.x) - 4.0f, hovered, extend, delta);
        draw.a(matrices, boxX, boxY, boxWidth, boxHeight, 2.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.039215688f * extend));
        draw.a(matrices, boxX, boxY, boxWidth, boxHeight, 2.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).getAlphaFloat() * extend));
        ScissorUtil.a(matrices, boxX, boxY, boxWidth, boxHeight);
        if (reverse > 0.0f) {
            Fonts.c.a(matrices, value, boxX + 4.0f, textY, 6.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT).toIntColor(), extend * reverse));
        }
        if (anim > 0.0f) {
            Fonts.c.a(matrices, "...", boxX + 4.0f, textY, 6.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT).toIntColor(), extend * anim));
        }
        ScissorUtil.a(matrices);
    }
}
