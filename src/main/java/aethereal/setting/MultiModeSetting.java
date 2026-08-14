package aethereal.setting;

import aethereal.ui.element.Element;
import aethereal.ui.element.MultiModeElement;

import java.util.Arrays;
import java.util.List;

public class MultiModeSetting extends Setting<List<BooleanSetting>> {
    public MultiModeSetting(String name, BooleanSetting... strings) {
        super(name, Arrays.asList(strings));
    }

    @Override
    public Element<?> createBooleanElement() {
        return new MultiModeElement(this);
    }

    public BooleanSetting a(String settingName) {
        return c().stream().filter(booleanSetting -> {
            return booleanSetting.i().equalsIgnoreCase(settingName);
        }).findFirst().orElse(null);
    }

    public BooleanSetting a(int index) {
        if (index >= 0 && index < c().size()) {
            return c().get(index);
        }
        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for size " + c().size());
    }
}
