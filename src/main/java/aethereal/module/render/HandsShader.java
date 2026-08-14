package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.HandEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.SliderSetting;
import aethereal.ui.shader.NoiseShader;
import net.minecraft.client.option.Perspective;

@ModuleRegister(a = "Hands Shader", b = "Накладывает шейдер на руку от первого лица", c = Category.Render)
public class HandsShader extends Module {
    private final SliderSetting b = new SliderSetting("Непрозрачность", 0.6f, 0.0f, 1.0f, 0.05f);

    public HandsShader() {
        a(this.b);
    }

    @EventTarget
    public void a(HandEvent event) {
        NoiseShader shader = Delta.h().d().i().f();
        if (mc.options.getPerspective() == Perspective.FIRST_PERSON) {
            if (event.b()) {
                shader.e();
            }
            if (event.c()) {
                float[] color = ColorUtil.a(Delta.h().d().o().a(ThemeInfo.PRIMARY).a());
                color[3] = this.b.c().floatValue();
                shader.a(color);
            }
        }
    }
}
