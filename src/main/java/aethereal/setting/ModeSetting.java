package aethereal.setting;

import aethereal.ui.element.Element_2;
import aethereal.ui.element.ModeElement;
import lombok.Generated;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<String> {
    private final List<String> a;

    public ModeSetting(String name, String defaultVal, String... strings) {
        super(name, defaultVal);
        this.a = Arrays.asList(strings);
    }

    @Generated
    public List<String> k() {
        return this.a;
    }

    public boolean l(String settingName) {
        return c().equalsIgnoreCase(settingName);
    }

    @Override
    public Element_2<?> d() {
        return new ModeElement(this);
    }
}
