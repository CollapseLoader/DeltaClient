package aethereal.setting;

import aethereal.ui.element.BooleanElement;
import aethereal.ui.element.Element_2;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, Boolean defaultVal) {
        super(name, defaultVal);
    }

    @Override
    public Element_2<?> d() {
        return new BooleanElement(this);
    }
}
