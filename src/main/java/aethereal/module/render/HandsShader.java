package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.*;
import aethereal.core.Module;
import aethereal.event.HandEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.SliderSetting;
import aethereal.ui.shader.NoiseShader;
import net.minecraft.client.option.Perspective;

@ModuleRegister(name = "Hands Shader", description = "Накладывает шейдер на руку от первого лица", category = Category.Render)
public class HandsShader extends Module {
    private final SliderSetting b = new SliderSetting("Непрозрачность", 0.6f, 0.0f, 1.0f, 0.05f);

    public HandsShader() {
        a(this.b);
    }

    @EventTarget
    public void a(HandEvent event) {
        NoiseShader shader = Delta.getInstance().getModuleProcessor().i().f();
        if (mc.options.getPerspective() == Perspective.FIRST_PERSON) {
            if (event.b()) {
                shader.e();
            }
            if (event.c()) {
                float[] color = ColorUtil.a(Delta.getInstance().getModuleProcessor().o().a(ThemeInfo.PRIMARY).toIntColor());
                color[3] = this.b.c().floatValue();
                shader.a(color);
            }
        }
    }
}
