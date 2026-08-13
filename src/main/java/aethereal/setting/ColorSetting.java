package aethereal.setting;

import aethereal.ui.element.ColorElement;
import aethereal.ui.element.Element_2;

public class ColorSetting extends Setting<Integer> {
    public ColorSetting(String name, Integer defaultVal) {
        super(name, defaultVal);
    }

    @Override
    public Element_2<?> d() {
        return new ColorElement(this);
    }
}
