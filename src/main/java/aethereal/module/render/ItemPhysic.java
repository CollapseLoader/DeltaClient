package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.BooleanSetting;
import lombok.Generated;

@ModuleRegister(a = "Item Physic", b = "Добавляет физику предметам, лежащим на земле", c = Category.Render)
public class ItemPhysic extends Module {
    private final BooleanSetting b = new BooleanSetting("Уменьшить размер предметов", false);

    public ItemPhysic() {
        a(this.b);
    }

    @Generated
    public BooleanSetting q() {
        return this.b;
    }
}
