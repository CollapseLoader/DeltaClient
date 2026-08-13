package aethereal.setting;

import aethereal.core.Action;
import aethereal.ui.element.BindElement;
import aethereal.ui.element.Element_2;
import lombok.Generated;

public class BindSetting extends Setting<Integer> {
    private final int c;
    private Action a;
    private Action b;

    public BindSetting(String name, Integer defaultVal) {
        super(name, defaultVal);
        this.c = 1;
    }

    public BindSetting(String name, Integer defaultVal, int type) {
        super(name, defaultVal);
        this.c = type;
    }

    @Generated
    public Action k() {
        return this.a;
    }

    @Generated
    public Action l() {
        return this.b;
    }

    @Generated
    public int m() {
        return this.c;
    }

    public BindSetting a(Action action) {
        this.a = action;
        return this;
    }

    public BindSetting b(Action release) {
        this.b = release;
        return this;
    }

    @Override
    public Element_2<?> d() {
        return new BindElement(this);
    }
}
