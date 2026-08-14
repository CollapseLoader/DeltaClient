package aethereal.setting;

import aethereal.ui.element.Element;
import aethereal.ui.element.ModeElement;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting<String> {
    private final List<String> modes;

    public ModeSetting(String name, String defaultVal, String... strings) {
        super(name, defaultVal);
        this.modes = Arrays.asList(strings);
    }

    public List<String> k() {
        return this.modes;
    }

    public boolean l(String settingName) {
        return c().equalsIgnoreCase(settingName);
    }

    @Override
    public Element<?> createBooleanElement() {
        return new ModeElement(this);
    }
}
