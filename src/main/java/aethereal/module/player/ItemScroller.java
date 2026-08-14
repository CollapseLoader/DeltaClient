package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;

@ModuleRegister(name = "Item Scroller", description = "Позволяет быстро перекладывать предметы в окнах прокруткой", category = Category.Player)
public class ItemScroller extends Module {
    private final SliderSetting b = new SliderSetting("Задержка между слотами", 50.0f, 0.0f, 100.0f, 1.0f);
    private final CounterUtil c = new CounterUtil();

    public ItemScroller() {
        a(this.b);
    }

    public SliderSetting q() {
        return this.b;
    }

    public CounterUtil r() {
        return this.c;
    }
}
