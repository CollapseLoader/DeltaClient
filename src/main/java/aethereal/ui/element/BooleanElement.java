package aethereal.ui.element;


import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.core.InterfaceC0020Opcode;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.setting.BooleanSetting;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector4f;

public class BooleanElement extends Element<BooleanSetting> {
    public BooleanElement(BooleanSetting setting) {
        super(setting);
        this.a.w = 11.0f;
    }

    @Override

    public boolean onMouseClick(double mouseX, double mouseY, int button) {
        Vector4f vector4f = this.a;
        var setting = this.b;
        if (!MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.w)) {
            return false;
        }
        if (button != 0) {
            if (button != 2) {
                return false;
            }
            if (!(setting instanceof BooleanSetting)) {
                throw new ClassCastException();
            }
            setting.b();
            return true;
        }
        if (!(setting instanceof BooleanSetting)) {
            throw new ClassCastException();
        }
        BooleanSetting booleanSetting = setting;
        Boolean boolC = booleanSetting.c();
        if (!(boolC instanceof Boolean)) {
            throw new ClassCastException();
        }
        booleanSetting.a(Boolean.valueOf(!boolC.booleanValue()));
        return true;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        getActivationAnimation().a(this.b.c().booleanValue());
        getActivationAnimation().a(0.0f, 1.0f, 0.5f, EasingList.i, delta);
        float enabled = getActivationAnimation().c();
        float disabled = 1.0f - enabled;
        float centerY = this.a.y + (this.a.w / 2.0f) + 0.5f;
        boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0f;
        drawLabel(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y, this.a.w, 6.5f, theme.a(ThemeInfo.TEXT).toIntColor(), (this.a.z - 11.0f) - 4.0f, hovered, extend, delta);
        float boxX = (this.a.x + this.a.z) - 11.0f;
        float boxY = centerY - 5.5f;
        draw.a(matrices, boxX, boxY, 11.0f, 11.0f, 3.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), 0.039215688f * extend));
        draw.a(matrices, boxX, boxY, 11.0f, 11.0f, 3.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).getAlphaFloat() * extend));
        if (disabled > 0.0f) {
            Fonts.a.a(matrices, "u", boxX + ((11.0f - Fonts.a.b("u", 6.0f)) / 2.0f) + 0.25f, Fonts.a.a("u", 6.0f, centerY), 6.0f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(InterfaceC0020Opcode.aN, 25, 25, 255), extend * disabled));
        }
        if (enabled > 0.0f) {
            Fonts.a.a(matrices, "m", boxX + ((11.0f - Fonts.a.b("m", 9.0f)) / 2.0f), Fonts.a.a("m", 9.0f, centerY), 9.0f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(InterfaceC0020Opcode.bW, 220, InterfaceC0020Opcode.ap, 255), extend * enabled));
        }
    }

    @Override
    public void onDrawEvent(DrawEvent event, float x, float y, float width, float animation) {
        getActivationAnimation().a(this.b.c().booleanValue());
        getActivationAnimation().a(0.0f, 1.0f, 0.3f, EasingList.g, event.g());
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        float textX = x + 19.5f;
        float toggleX = ((x + width) - 11.0f) - 5.0f;
        float toggleY = y + 2.25f;
        int primary = theme.a(ThemeInfo.PRIMARY).toIntColor();
        Fonts.a.a(event.h(), "g", x + 5.0f, y + ((12.0f - Fonts.a.a(6.5f)) / 2.0f), 6.5f, ColorUtil.applyAlphaToColor(primary, animation));
        event.getDraw2DProcessor().a(event.i().getMatrices(), x + 15.5f, y + 3.0f, 0.75f, 6.0f, 0.0f, ColorUtil.applyAlphaToColor(ColorUtil.convertToARGB(InterfaceC0020Opcode.aN, InterfaceC0020Opcode.aN, InterfaceC0020Opcode.aN, 255), 0.5f * animation));
        Fonts.e.a(event.h(), this.b.i(), textX, (y + ((12.0f - Fonts.e.a(6.5f)) / 2.0f)) - 0.5f, 6.5f, ColorUtil.applyAlphaToColor(-1, animation));
        float value = getActivationAnimation().c();
        event.getDraw2DProcessor().a(event.h(), toggleX, toggleY, 11.0f, 7.5f, 2.5f, ColorUtil.applyAlphaToColor(primary, value * animation));
        event.getDraw2DProcessor().a(event.h(), toggleX, toggleY, 11.0f, 7.5f, 2.5f, 0.3f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).getAlphaFloat() * animation));
        event.getDraw2DProcessor().a(event.h(), toggleX + 1.5f + (3.5f * value), toggleY + 1.5f, 4.5f, 4.5f, 1.25f, ColorUtil.applyAlphaToColor(ColorUtil.lerpColor(ColorUtil.convertToARGB(InterfaceC0020Opcode.ap, InterfaceC0020Opcode.ap, InterfaceC0020Opcode.bk, 255), ColorUtil.convertToARGB(255, 255, 255, 255), value), animation));
    }
}
