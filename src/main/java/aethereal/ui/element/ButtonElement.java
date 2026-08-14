package aethereal.ui.element;


import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.ButtonSetting;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector4f;

public class ButtonElement extends Element<ButtonSetting> {
    public ButtonElement(ButtonSetting setting) {
        super(setting);
        this.a.w = 14.0f;
    }

    @Override

    public boolean onMouseClick(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return false;
        }
        Vector4f vector4f = this.a;
        var setting = this.b;
        if (!MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.w)) {
            return false;
        }
        if (!(setting instanceof ButtonSetting)) {
            throw new ClassCastException();
        }
        setting.k();
        return true;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        getActivationAnimation().a(MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0f);
        getActivationAnimation().a(0.0f, 1.0f, 0.3f, EasingList.i, delta);
        float hover = getActivationAnimation().c();
        int text = ColorUtil.convertToARGB(255, 255, 255, 255);
        draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 4.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), ((10.0f + (20.0f * hover)) / 255.0f) * extend));
        draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 4.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_MEDIUM).toIntColor(), theme.a(ThemeInfo.OUTLINE_MEDIUM).getAlphaFloat() * extend));
        Fonts.c.b(matrices, this.b.i(), this.a.x + (this.a.z / 2.0f), (this.a.y + ((this.a.w - Fonts.c.a(7.0f)) / 2.0f)) - 0.5f, 7.0f, ColorUtil.applyAlphaToColor(text, extend));
    }
}
