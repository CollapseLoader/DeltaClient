package aethereal.setting;

import aethereal.ui.element.Element;
import aethereal.ui.element.SliderElement;

public class SliderSetting extends Setting<Float> {
    public final float a;
    public final float b;
    public final float c;
    public final float d;
    public final boolean e;

    public SliderSetting(String name, float defaultVal, float min, float max, float increment) {
        this(name, defaultVal, min, max, increment, false);
    }

    public SliderSetting(String name, float defaultVal, float min, float max, float increment, boolean scroll) {
        super(name, Float.valueOf(defaultVal));
        this.a = min;
        this.b = max;
        this.d = defaultVal;
        this.c = increment;
        this.e = scroll;
    }

    @Override
    public Element<?> createBooleanElement() {
        return new SliderElement(this);
    }
}
