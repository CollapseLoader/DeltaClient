package aethereal.setting;

import aethereal.ui.element.BooleanElement;
import aethereal.ui.element.Element;

public class BooleanSetting extends Setting<Boolean> {
    public BooleanSetting(String name, Boolean defaultVal) {
        super(name, defaultVal);
    }

    @Override
    public Element<?> createBooleanElement() {
        return new BooleanElement(this);
    }
}
