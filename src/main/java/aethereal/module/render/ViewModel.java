package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.HandViewEvent;
import aethereal.setting.ButtonSetting;
import aethereal.setting.SliderSetting;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Hand;

@ModuleRegister(name = "View Model", description = "Изменяет положение и размер предметов в руке", category = Category.Render)
public class ViewModel extends Module {
    private final SliderSetting b = new SliderSetting("Основная рука X", 0.0f, -2.0f, 2.0f, 0.1f);
    private final SliderSetting c = new SliderSetting("Основная рука Y", 0.0f, -2.0f, 2.0f, 0.1f);
    private final SliderSetting d = new SliderSetting("Основная рука Z", 0.0f, -2.0f, 2.0f, 0.1f);
    private final SliderSetting e = new SliderSetting("Вторая рука X", 0.0f, -2.0f, 2.0f, 0.1f);
    private final SliderSetting f = new SliderSetting("Вторая рука Y", 0.0f, -2.0f, 2.0f, 0.1f);
    private final SliderSetting g = new SliderSetting("Вторая рука Z", 0.0f, -2.0f, 2.0f, 0.1f);

    public ViewModel() {
        ButtonSetting h = new ButtonSetting("Сбросить позиции", () -> {
            e().forEach(setting -> {
                if (setting instanceof SliderSetting slider) {
                    slider.a(slider.g());
                }
            });
        });
        a(this.b, this.c, this.d, this.e, this.f, this.g, h);
    }

    @EventTarget
    public void a(HandViewEvent e) {
        MatrixStack matrix = e.b();
        if (e.d().equals(Hand.MAIN_HAND)) {
            matrix.translate(this.b.h().floatValue(), this.c.h().floatValue(), this.d.h().floatValue());
        } else {
            matrix.translate(this.e.h().floatValue(), this.f.h().floatValue(), this.g.h().floatValue());
        }
    }
}
