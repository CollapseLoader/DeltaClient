package aethereal.setting;

import aethereal.ui.element.ColorElement;
import aethereal.ui.element.Element;

public class ColorSetting extends Setting<Integer> {
    public ColorSetting(String name, Integer defaultVal) {
        super(name, defaultVal);
    }

    @Override
    public Element<?> createBooleanElement() {
        return new ColorElement(this);
    }
}
