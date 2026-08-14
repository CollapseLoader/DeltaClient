package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PushEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;

@ModuleRegister(name = "No Push", description = "Отключает отталкивание от выбранных объектов", category = Category.Movement)
public class NoPush extends Module {
    private final MultiModeSetting b = new MultiModeSetting("Отключить коллизию для", new BooleanSetting("Воды и лавы", false), new BooleanSetting("Блоков", false), new BooleanSetting("Энтити", false), new BooleanSetting("Граница", false), new BooleanSetting("Удочки", false));

    public NoPush() {
        a(this.b);
    }

    public MultiModeSetting q() {
        return this.b;
    }

    @EventTarget
    public void a(PushEvent event) {
        switch (event.b()) {
            case FLUIDS:
                event.a(this.b.a("Воды и лавы").c().booleanValue());
                break;
            case BLOCKS:
                event.a(this.b.a("Блоков").c().booleanValue());
                break;
            case ENTITIES:
                event.a(this.b.a("Энтити").c().booleanValue());
                break;
            case WORLD_BORDER:
                event.a(this.b.a("Граница").c().booleanValue());
                break;
            case FISHING_HOOK:
                event.a(this.b.a("Удочки").c().booleanValue());
                break;
        }
    }
}
