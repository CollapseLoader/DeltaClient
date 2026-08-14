package aethereal.ui.element;


import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Delta;
import aethereal.render.*;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.util.MathUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Vector4f;

import java.util.List;
import java.util.function.Consumer;

public class MultiModeElement extends Element<MultiModeSetting> {
    private final AnimationUtil[] modeAnimations;

    public MultiModeElement(MultiModeSetting setting) {
        super(setting);
        this.modeAnimations = new AnimationUtil[setting.c().size()];
        for (int i = 0; i < this.modeAnimations.length; i++) {
            this.modeAnimations[i] = new AnimationUtil();
        }
    }

    @Override

    public boolean onMouseClick(double mouseX, double mouseY, int button) {
        Vector4f vector4f = this.a;
        var setting = this.b;
        if (button != 0) {
            if (button != 2 || !MathUtil.a(mouseX, mouseY, vector4f.x, vector4f.y, vector4f.z, vector4f.w)) {
                return false;
            }
            if (!(setting instanceof MultiModeSetting)) {
                throw new ClassCastException();
            }
            List<BooleanSetting> listC = setting.c();
            if (!(listC instanceof List)) {
                throw new ClassCastException();
            }
            listC.forEach(new Consumer<BooleanSetting>() {
                @Override
                public void accept(BooleanSetting obj) {
                    obj.b();
                }
            });
            return true;
        }
        float f = vector4f.x;
        float fA = vector4f.y + Fonts.c.a(6.5f) + 5.0f;
        if (!(setting instanceof MultiModeSetting)) {
            throw new ClassCastException();
        }
        List<BooleanSetting> listC2 = setting.c();
        if (!(listC2 instanceof List)) {
            throw new ClassCastException();
        }
        for (BooleanSetting booleanSetting : listC2) {
            if (!(booleanSetting instanceof BooleanSetting)) {
                throw new ClassCastException();
            }
            BooleanSetting booleanSetting2 = booleanSetting;
            float fA2 = Fonts.c.a(booleanSetting2.i(), 6.25f) + 6.0f;
            if (f + fA2 > vector4f.x + vector4f.z) {
                f = vector4f.x;
                fA += 12.0f;
            }
            if (MathUtil.a(mouseX, mouseY, f, fA, fA2, 9.0f)) {
                Boolean boolC = booleanSetting2.c();
                if (!(boolC instanceof Boolean)) {
                    throw new ClassCastException();
                }
                booleanSetting2.a(Boolean.valueOf(!boolC.booleanValue()));
                return true;
            }
            f += fA2 + 3.0f;
        }
        return false;
    }

    @Override
    public void render(DrawContext context, double mouseX, double mouseY, float delta, float extend) {
        MatrixStack matrices = context.getMatrices();
        Draw2DProcessor draw = Delta.getInstance().getModuleProcessor().i();
        ThemeProcessor theme = Delta.getInstance().getModuleProcessor().o();
        long selectedCount = this.b.c().stream().filter((v0) -> {
            return v0.c();
        }).count();
        this.b.c().size();
        String counter = selectedCount + " из " + selectedCount;
        float counterWidth = Fonts.c.a(counter, 6.5f);
        boolean hovered = MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w) && extend >= 1.0f;
        drawLabel(matrices, Fonts.c, this.b.i(), this.a.x, this.a.y, Fonts.c.a(6.5f) + 1.0f, 6.5f, theme.a(ThemeInfo.TEXT).toIntColor(), (this.a.z - counterWidth) - 4.0f, hovered, extend, delta);
        Fonts.c.a(matrices, counter, (this.a.x + this.a.z) - counterWidth, this.a.y + 0.25f, 6.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), extend));
        float x = this.a.x;
        float y = this.a.y + Fonts.c.a(6.5f) + 5.0f;
        int i = 0;
        for (BooleanSetting mode : this.b.c()) {
            float width = Fonts.c.a(mode.i(), 6.25f) + 6.0f;
            if (x + width > this.a.x + this.a.z) {
                x = this.a.x;
                y += 12.0f;
            }
            this.modeAnimations[i].a(mode.c().booleanValue());
            this.modeAnimations[i].a(0.0f, 1.0f, 0.3f, EasingList.i, delta);
            float value = this.modeAnimations[i].c();
            draw.a(matrices, x, y, width, 9.0f, 2.0f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.PRIMARY).toIntColor(), ((5.0f + (65.0f * value)) / 255.0f) * extend));
            draw.a(matrices, x, y, width, 9.0f, 2.0f, 0.5f, ColorUtil.applyAlphaToColor(theme.a(ThemeInfo.OUTLINE_SMALL).toIntColor(), theme.a(ThemeInfo.OUTLINE_SMALL).getAlphaFloat() * extend));
            int color = ColorUtil.lerpColor(theme.a(ThemeInfo.TEXT_DISABLED).toIntColor(), theme.a(ThemeInfo.TEXT).toIntColor(), value);
            Fonts.c.b(matrices, mode.i(), x + (width / 2.0f), (y + ((9.0f - Fonts.c.a(6.25f)) / 2.0f)) - 0.75f, 6.25f, ColorUtil.applyAlphaToColor(color, extend));
            x += width + 3.0f;
            i++;
        }
        this.a.w = (y + 9.0f) - this.a.y;
    }
}
