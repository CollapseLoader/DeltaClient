package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.BooleanSetting;

@ModuleRegister(name = "Item Physic", description = "Добавляет физику предметам, лежащим на земле", category = Category.Render)
public class ItemPhysic extends Module {
    private final BooleanSetting b = new BooleanSetting("Уменьшить размер предметов", false);

    public ItemPhysic() {
        a(this.b);
    }

    public BooleanSetting q() {
        return this.b;
    }
}
