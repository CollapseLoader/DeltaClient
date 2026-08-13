package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.SoundEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.SliderSetting;

@ModuleRegister(a = "Sound Reducer", b = "Уменьшает громкость выбранных игровых звуков", c = Category.Player)
public class SoundReducer extends Module {
    private final ModeSetting b = new ModeSetting("Способ обработки звуков", "Приглушение", "Приглушение", "Отключение");
    private final SliderSetting c = new SliderSetting("Громкость производящего звука", 0.5f, 0.0f, 1.0f, 0.01f).a(() -> {
        return Boolean.valueOf(this.b.l("Приглушение"));
    });
    private final MultiModeSetting d = new MultiModeSetting("Выберите звуки", new BooleanSetting("Бросание зелей/опыта", true), new BooleanSetting("Взрыв Крипера", false), new BooleanSetting("Взрыв TNT", false), new BooleanSetting("Наковальни", true), new BooleanSetting("Выстрел лука", false), new BooleanSetting("Крик Гаста", false), new BooleanSetting("Шаги скелета", false), new BooleanSetting("Звук портала в ад", false), new BooleanSetting("Ивента", true), new BooleanSetting("Иссушения", true), new BooleanSetting("Фантомы", true), new BooleanSetting("Трезубца", true), new BooleanSetting("Тотема", false), new BooleanSetting("Вардена", false), new BooleanSetting("Скалк Сенсор", false));

    public SoundReducer() {
        a(this.b, this.d, this.c);
    }

    @EventTarget
    public void a(SoundEvent event) {
        if (a(event.b().getId().getPath())) {
            event.a(this.b.l("Отключение") ? 0.0f : event.c() * this.c.c().floatValue());
        }
    }

    public boolean a(String path) {
        return (this.d.a("Бросание зелей/опыта").c().booleanValue() && (path.contains("splash_potion") || path.contains("experience_orb") || path.contains("lingering_potion"))) || (this.d.a("Взрыв Крипера").c().booleanValue() && (path.contains("creeper") || (path.contains("generic") && path.contains("explode")))) || ((this.d.a("Взрыв TNT").c().booleanValue() && (path.contains("tnt") || (path.contains("generic") && path.contains("explode")))) || ((this.d.a("Наковальни").c().booleanValue() && path.contains("anvil")) || ((this.d.a("Выстрел лука").c().booleanValue() && (path.contains("arrow") || path.contains("bow"))) || ((this.d.a("Крик Гаста").c().booleanValue() && path.contains("ghast")) || ((this.d.a("Шаги скелета").c().booleanValue() && path.contains("skeleton") && !path.contains("skeleton_horse")) || ((this.d.a("Звук портала в ад").c().booleanValue() && path.contains("portal")) || ((this.d.a("Ивента").c().booleanValue() && path.contains("entity.ender_dragon.ambient")) || ((this.d.a("Иссушения").c().booleanValue() && path.contains("wither") && !path.contains("wither_skeleton")) || ((this.d.a("Фантомы").c().booleanValue() && path.contains("phantom")) || ((this.d.a("Трезубца").c().booleanValue() && path.contains("trident")) || ((this.d.a("Тотема").c().booleanValue() && path.contains("totem")) || ((this.d.a("Вардена").c().booleanValue() && path.contains("warden")) || (this.d.a("Скалк Сенсор").c().booleanValue() && path.contains("sculk"))))))))))))));
    }
}
