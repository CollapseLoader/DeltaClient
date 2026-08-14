package aethereal.setting;

import aethereal.ui.element.Element;
import aethereal.ui.element.StringElement;

public class StringSetting extends Setting<String> {
    private final boolean allowNumbers;

    public StringSetting(String name, String defaultVal) {
        super(name, defaultVal);
        this.allowNumbers = false;
    }

    public StringSetting(String name, String defaultVal, boolean numbers) {
        super(name, defaultVal);
        this.allowNumbers = numbers;
    }

    public boolean k() {
        return this.allowNumbers;
    }

    @Override
    public Element<?> createBooleanElement() {
        return new StringElement(this);
    }
}
