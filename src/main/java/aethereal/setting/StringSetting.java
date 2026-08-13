package aethereal.setting;

import aethereal.ui.element.Element_2;
import aethereal.ui.element.StringElement;
import lombok.Generated;

public class StringSetting extends Setting<String> {
    private final boolean a;

    public StringSetting(String name, String defaultVal) {
        super(name, defaultVal);
        this.a = false;
    }

    public StringSetting(String name, String defaultVal, boolean numbers) {
        super(name, defaultVal);
        this.a = numbers;
    }

    @Generated
    public boolean k() {
        return this.a;
    }

    @Override
    public Element_2<?> d() {
        return new StringElement(this);
    }
}
